package com.example.data_server.service;

import com.example.data_server.repository.BusinessRepository;
import com.example.data_server.repository.CustomerRepository;
import com.example.data_server.repository.OfferRepository;
import com.example.sep3.grpc.*;
import com.example.data_server.repository.OrderRepository;
import com.example.shared.dao.domainDao.OfferDao;
import com.example.shared.dao.domainDao.OrderDao;
import com.example.shared.dao.usersDao.BusinessDao;
import com.example.shared.converters.DateTimeConverter;
import com.example.shared.dao.usersDao.CustomerDao;
import com.example.shared.model.OfferStatus;
import com.example.shared.model.OrderStatus;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@GrpcService public class OrderServiceImpl
    extends OrderServiceGrpc.OrderServiceImplBase
{

  private final OrderRepository orderRepository;
  private final OfferRepository offerRepository;
  private final BusinessRepository businessRepository;
  private final CustomerRepository customerRepository;

  @Autowired public OrderServiceImpl(OrderRepository orderRepository,
      OfferRepository offerRepository, BusinessRepository businessRepository,
      CustomerRepository customerRepository)
  {
    this.orderRepository = orderRepository;
    this.offerRepository = offerRepository;
    this.businessRepository = businessRepository;
    this.customerRepository = customerRepository;
    System.out.println("OrderServiceImpl created");
  }

  @Override public void addOrder(AddOrderRequest request,
      StreamObserver<OrderResponse> responseObserver)
  {
    System.out.println("Request to add Order");

    OrderDao Order = generateOrderDaoFromAddOrderRequest(request);
    OfferDao offer = offerRepository.findById(request.getOfferId()).get();

    if(offer.getNumberOfAvailableItems() == request.getNumberOfItems())
      offerRepository.updateStatus(offer.getId(), OfferStatus.RESERVED.getStatus());

    offerRepository.updateNumberOfAvailableItems(offer.getId(),
        offer.getNumberOfAvailableItems() - request.getNumberOfItems());


    OrderDao createdOrder = orderRepository.save(Order);

    buildOrderResponseFromOrderDao(responseObserver, createdOrder);
  }

  @Override public void getOrderById(OrderIdRequest request,
      StreamObserver<OrderResponse> responseObserver)
  {
    System.out.println("Request for Order by id");

    Optional<OrderDao> Order = orderRepository.findById(request.getId());
    if (Order.isPresent())
    {
      OrderDao orderDao = Order.get();
      buildOrderResponseFromOrderDao(responseObserver, orderDao);
    }
    else
    {
      responseObserver.onError(
          new Exception("Error: No Order with ID " + request.getId()));
    }
  }

  @Override public void getAllOrders(IdRequestResponse request,
      StreamObserver<OrderList> responseObserver)
  {
    System.out.println("Request for all Orders");

    //TODO: only fetch the ones which match the id
    List<OrderDao> Orders = orderRepository.findAll();

    OrderList.Builder OrderListBuilder = OrderList.newBuilder();
    for (OrderDao orderDao : Orders)
    {
      OrderResponse response = generateOrderResponseFromOrderDao(orderDao);
      OrderListBuilder.addOrders(response);
    }

    OrderList OrderListResponse = OrderListBuilder.build();
    responseObserver.onNext(OrderListResponse);
    responseObserver.onCompleted();
  }

  @Override public void updateOrderStatus(OrderStatusRequest orderStatusRequest,
      StreamObserver<OrderResponse> responseObserver)
  {
    System.out.println("Request to update order status.");

    OrderDao orderDao = orderRepository.findById(orderStatusRequest.getId())
        .orElseThrow(() -> new IllegalArgumentException("Order not found"));

    orderDao.setStatus(orderStatusRequest.getStatus());
    orderRepository.save(orderDao);

    buildOrderResponseFromOrderDao(responseObserver, orderDao);
  }

  private void buildOrderResponseFromOrderDao(
      StreamObserver<OrderResponse> responseObserver, OrderDao orderDao)
  {
    OrderResponse response = generateOrderResponseFromOrderDao(orderDao);

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  private OrderResponse generateOrderResponseFromOrderDao(OrderDao orderDao)
  {
    return OrderResponse.newBuilder().setId(orderDao.getId())
        .setCustomerId(orderDao.getCustomer().getId())
        .setOfferId(orderDao.getOffer().getId())
        .setNumberOfItems(orderDao.getNumberOfItems()).setOrderTime(
            DateTimeConverter.convertLocalDateTime_To_ProtoTimestamp(
                orderDao.getOrderTime())).setStatus(orderDao.getStatus())
        .setPricePerItem(orderDao.getPricePerItem())
        .setBusinessName(orderDao.getBusiness().getBusinessName()).build();
  }

  private OrderDao generateOrderDaoFromAddOrderRequest(AddOrderRequest request)
  {
    OrderDao order = new OrderDao();

    // Extract customer
    Optional<CustomerDao> customerOptional = customerRepository.findById(
        request.getCustomerId());
    if (customerOptional.isEmpty())
      throw new IllegalArgumentException(
          "Customer not found with ID: " + request.getCustomerId());

    CustomerDao customer = customerOptional.get();
    order.setCustomer(customer);

    order.setNumberOfItems(request.getNumberOfItems());
    order.setOrderTime(LocalDateTime.now());
    order.setStatus(OrderStatus.PENDING.getStatus());

    Optional<OfferDao> offer = offerRepository.findById(request.getOfferId());
    if (offer.isPresent())
    {
      OfferDao offerDao = offer.get();
      order.setPricePerItem(offerDao.getOfferPrice());
      order.setOffer(offerDao);

      //Extract business
      Optional<BusinessDao> businessOptional = businessRepository.findById(
          offerDao.getBusiness().getId());
      if (businessOptional.isEmpty())
        throw new IllegalArgumentException(
            "Business not found with ID: " + offerDao.getBusiness().getId());
      BusinessDao business = businessOptional.get();
      order.setBusiness(business);
    }
    else
    {
      throw new IllegalArgumentException(
          "Error: No offer with ID " + request.getOfferId());
    }

    return order;
  }

}
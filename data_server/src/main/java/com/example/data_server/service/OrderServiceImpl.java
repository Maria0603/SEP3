package com.example.data_server.service;

import com.example.data_server.repository.OfferRepository;
import com.example.sep3.grpc.*;
import com.example.data_server.repository.OrderRepository;
import com.example.shared.dao.OfferDao;
import com.example.shared.dao.OrderDao;
import com.example.data_server.utility.DateTimeConverter;
import com.example.shared.model.OrderStatus;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@GrpcService public class OrderServiceImpl
    extends OrderServiceGrpc.OrderServiceImplBase
{

  private final OrderRepository orderRepository;
  private final OfferRepository offerRepository;

  @Autowired public OrderServiceImpl(OrderRepository orderRepository,
      OfferRepository offerRepository)
  {
    this.orderRepository = orderRepository;
    this.offerRepository = offerRepository;
  }

  @Override public void addOrder(AddOrderRequest request,
      StreamObserver<OrderResponse> responseObserver)
  {
    System.out.println("Request to add Order");

    OrderDao Order = generateOrderDaoFromAddOrderRequest(request);
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

  @Override public void getAllOrders(EmptyMessage request,
      StreamObserver<OrderList> responseObserver)
  {
    System.out.println("Request for all Orders");

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
        .setUserId(orderDao.getUserId()).setOfferId(orderDao.getOffer().getId())
        .setQuantity(orderDao.getQuantity()).setOrderDate(
            DateTimeConverter.convertDateDaoToGrpcDate(orderDao.getOrderDate()))
        .setOrderTime(
            DateTimeConverter.convertTimeDaoToGrpcTime(orderDao.getOrderTime()))
        .setStatus(orderDao.getStatus())
        .setPricePerItem(orderDao.getPricePerItem())
        .setTotalPrice(orderDao.getTotalPrice()).build();
  }

  private OrderDao generateOrderDaoFromAddOrderRequest(AddOrderRequest request)
  {
    OrderDao order = new OrderDao();
    order.setUserId(request.getUserId());
    order.setQuantity(request.getQuantity());
    order.setOrderDate(DateTimeConverter.getCurrentDateDao());
    order.setOrderTime(DateTimeConverter.getCurrentTimeDao());
    order.setStatus(OrderStatus.PENDING.getStatus());

    Optional<OfferDao> offer = offerRepository.findById(request.getOfferId());
    if (offer.isPresent())
    {
      OfferDao offerDao = offer.get();
      order.setTotalPrice(offerDao.getOfferPrice() * request.getQuantity());
      order.setPricePerItem(offerDao.getOfferPrice());
      order.setOffer(offerDao);
    }
    else
    {
      throw new IllegalArgumentException(
          "Error: No offer with ID " + request.getOfferId());
    }

    return order;
  }

}
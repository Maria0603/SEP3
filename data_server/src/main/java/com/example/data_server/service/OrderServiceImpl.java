package com.example.data_server.service;

import com.example.sep3.grpc.*;
import com.example.data_server.repository.OrderRepository;
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
    extends OrderServiceGrpc.OrderServiceImplBase {

  private final OrderRepository OrderRepository;

  @Autowired public OrderServiceImpl(OrderRepository OrderRepository) {
    this.OrderRepository = OrderRepository;
  }

  @Override public void addOrder(AddOrderRequest request,
      StreamObserver<OrderResponse> responseObserver) {
    System.out.println("Request to add Order");

    OrderDao Order = generateOrderDaoFromAddOrderRequest(request);
    OrderDao createdOrder = OrderRepository.save(Order);
    buildOrderResponseFromOrderDao(responseObserver, createdOrder);
  }

  @Override public void getOrderById(OrderIdRequest request,
      StreamObserver<OrderResponse> responseObserver) {
    System.out.println("Request for Order by id");

    Optional<OrderDao> Order = OrderRepository.findById(request.getId());
    if (Order.isPresent()) {
      OrderDao orderDao = Order.get();
      buildOrderResponseFromOrderDao(responseObserver, orderDao);
    }
    else {
      responseObserver.onError(
          new Exception("Error: No Order with ID " + request.getId()));
    }
  }

  @Override public void getAllOrders(Empty request,
      StreamObserver<OrderList> responseObserver) {
    System.out.println("Request for all Orders");

    List<OrderDao> Orders = OrderRepository.findAll();

    OrderList.Builder OrderListBuilder = OrderList.newBuilder();
    for (OrderDao orderDao : Orders) {
      OrderResponse response = getOrderResponseFromOrderDao(orderDao);
      OrderListBuilder.addOrders(response);
    }

    OrderList OrderListResponse = OrderListBuilder.build();
    responseObserver.onNext(OrderListResponse);
    responseObserver.onCompleted();
  }

  private OrderResponse buildOrderResponseFromOrderDao(
      StreamObserver<OrderResponse> responseObserver, OrderDao orderDao) {
    OrderResponse response = getOrderResponseFromOrderDao(orderDao);
    responseObserver.onNext(response);
    responseObserver.onCompleted();
    return response;
  }

  private static OrderResponse getOrderResponseFromOrderDao(OrderDao orderDao) {
    OrderResponse response = OrderResponse.newBuilder()
        .setId(orderDao.getId()).setUserId(orderDao.getUserId())
        .setQuantity(orderDao.getQuantity()).setOrderDate(
            DateTimeConverter.convertDateDaoToGrpcDate(
                orderDao.getOrderDate())).setOrderTime(
            DateTimeConverter.convertTimeDaoToGrpcTime(
                orderDao.getOrderTime())).setStatus(orderDao.getStatus())
        .setOriginalPrice(orderDao.getOriginalPrice())
        .setOfferPrice(orderDao.getOfferPrice()).build();
    return response;
  }

 private static OrderDao generateOrderDaoFromAddOrderRequest(AddOrderRequest request, OrderRepository orderRepository) {
    OrderDao order = new OrderDao();
    order.setUserId(request.getUserId());
    order.setQuantity(request.getQuantity());
    order.setOrderDate(DateTimeConverter.convertGrpcDateToDateDao(request.getOrderDate()));
    order.setOrderTime(DateTimeConverter.convertGrpcTimeToTimeDao(request.getOrderTime()));
    order.setStatus(OrderStatus.RESERVED.getStatus());

    Optional<OrderDao> existingOrder = orderRepository.findById(request.getOfferId());
    if (existingOrder.isPresent()) {
        OrderDao existingOrderDao = existingOrder.get();
        order.setOriginalPrice(existingOrderDao.getOriginalPrice() * request.getQuantity());
        order.setOfferPrice(existingOrderDao.getOfferPrice() * request.getQuantity());
    } else {
        throw new IllegalArgumentException("Error: No offer with ID " + request.getOfferId());
    }

    return order;
}

}
package com.example.data_server.service;

import com.example.data_server.repository.OrderRepository;
import com.example.data_server.utility.DateTimeConverter;
import com.example.sep3.grpc.*;
import com.example.shared.dao.OrderDao;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

  @Mock private OrderRepository OrderRepository;

  @InjectMocks private OrderServiceImpl OrderService;

  @BeforeEach void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test void testAddOrder() {
    OrderDao Order = new OrderDao();
    Order.setId("Order123");
    Order.setUserId("user123");
    Order.setQuantity(2);
    Order.setOrderDate(DateTimeConverter.getCurrentDateDao());

    when(OrderRepository.save(any(OrderDao.class))).thenReturn(Order);

    AddOrderRequest request = AddOrderRequest.newBuilder()
        .setUserId("user123").setQuantity(2).setOrderDate(
            DateTimeConverter.convertDateDaoToGoogleDate(
                Order.getOrderDate())).build();

    StreamObserver<OrderResponse> responseObserver = mock(
        StreamObserver.class);
    OrderService.addOrder(request, responseObserver);

    verify(responseObserver).onNext(any(OrderResponse.class));
    verify(responseObserver).onCompleted();
  }

  @Test void testGetOrderById() {
    OrderDao Order = new OrderDao();
    Order.setId("Order123");
    Order.setUserId("user123");
    Order.setQuantity(2);
    Order.setOrderDate(DateTimeConverter.getCurrentDateDao());

    // Add the Order to the repository
    OrderRepository.save(Order);

    when(OrderRepository.findById("Order123")).thenReturn(
        Optional.of(Order));

    OrderIdRequest request = OrderIdRequest.newBuilder()
        .setId("Order123").build();

    StreamObserver<OrderResponse> responseObserver = mock(
        StreamObserver.class);
    OrderService.getOrderById(request, responseObserver);

    verify(responseObserver).onNext(argThat(
        response -> response.getId().equals("Order123")
            && response.getUserId().equals("user123")
            && response.getQuantity() == 2
            && response.getOrderDate().getYear()
            == DateTimeConverter.convertDateDaoToGoogleDate(
            Order.getOrderDate()).getYear()
            && response.getOrderDate().getMonth()
            == DateTimeConverter.convertDateDaoToGoogleDate(
            Order.getOrderDate()).getMonth()
            && response.getOrderDate().getDay()
            == DateTimeConverter.convertDateDaoToGoogleDate(
            Order.getOrderDate()).getDay()));
    verify(responseObserver).onCompleted();
  }

  @Test void testGetAllOrders() {
    OrderDao Order1 = new OrderDao();
    Order1.setId("Order123");
    Order1.setUserId("user123");
    Order1.setQuantity(2);
    Order1.setOrderDate(DateTimeConverter.getCurrentDateDao());

    OrderDao Order2 = new OrderDao();
    Order2.setId("Order456");
    Order2.setUserId("user456");
    Order2.setQuantity(3);
    Order2.setOrderDate(DateTimeConverter.getCurrentDateDao());

    when(OrderRepository.findAll()).thenReturn(
        List.of(Order1, Order2));

    StreamObserver<OrderList> responseObserver = mock(StreamObserver.class);
    OrderService.getAllOrders(Empty.newBuilder().build(),
        responseObserver);

    verify(responseObserver).onNext(any(OrderList.class));
    verify(responseObserver).onCompleted();
  }
}
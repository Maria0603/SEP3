package com.example.server.services;

import com.example.sep3.grpc.AddOrderRequest;
import com.example.sep3.grpc.OfferServiceGrpc;
import com.example.sep3.grpc.OrderResponse;
import com.example.sep3.grpc.OrderServiceGrpc;
import com.example.server.DataServerStub;
import com.example.server.converters.DtoGrpcConverter;
import com.example.server.dto.order.AddOrderRequestDto;
import com.example.server.dto.order.AddOrderResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service public class OrderService extends OrderServiceGrpc.OrderServiceImplBase {
  private final DataServerStub dataServerStub;

  @Autowired public OrderService(DataServerStub dataServerStub) {
    System.out.println("OrderService created");
    this.dataServerStub = dataServerStub;
  }

  @Transactional public AddOrderResponseDto addOrder(AddOrderRequestDto orderRequestDto) {
    AddOrderRequest request = DtoGrpcConverter.AddOrderRequestDto_To_AddOrderRequest(orderRequestDto);
    OrderResponse response = dataServerStub.addOrder(request);
    return DtoGrpcConverter.AddOrderResponseGrpc_To_AddOrderResponseDto(response);
}
}

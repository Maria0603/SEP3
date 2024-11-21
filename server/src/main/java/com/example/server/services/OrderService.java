package com.example.server.services;

import com.example.sep3.grpc.AddOrderRequest;
import com.example.sep3.grpc.OfferServiceGrpc;
import com.example.server.DataServerStub;
import com.example.server.converters.DtoGrpcConverter;
import com.example.server.dto.order.AddOrderRequestDto;
import com.example.server.dto.order.AddOrderResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service public class OrderService extends OfferServiceGrpc.OfferServiceImplBase {
  private final DataServerStub dataServerStub;

  @Autowired public OrderService(DataServerStub dataServerStub) {
    this.dataServerStub = dataServerStub;
    System.out.println("OrderService created");
  }

  @Transactional public AddOrderResponseDto addOrder(AddOrderRequestDto orderRequestDto) {
    // Transform the DTO to a gRPC message
    AddOrderRequest request = DtoGrpcConverter.AddOrderRequestDto_To_AddOrderRequest(orderRequestDto);


    // Send the request to the data server
    AddOrderResponseDto response = dataServerStub.saveOffer(request);

    // Transform the gRPC response to a DTO
    return DtoGrpcConverter.AddOrderResponseGrpc_To_AddOrderResponseDto(response);
}
}

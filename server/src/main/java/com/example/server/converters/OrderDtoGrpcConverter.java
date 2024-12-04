package com.example.server.converters;

import com.example.sep3.grpc.AddOrderRequest;
import com.example.sep3.grpc.OrderResponse;
import com.example.server.dto.order.AddOrderRequestDto;
import com.example.server.dto.order.OrderResponseDto;
import com.example.shared.converters.DateTimeConverter;

public class OrderDtoGrpcConverter
{

  public static AddOrderRequest AddOrderRequestDto_To_AddOrderRequest(
      AddOrderRequestDto addOrderRequestDto)
  {
    return AddOrderRequest.newBuilder()
        .setOfferId(addOrderRequestDto.getOfferId())
        .setNumberOfItems(addOrderRequestDto.getNumberOfItems())
        .setUserId(addOrderRequestDto.getUserId()).build();
  }

  public static OrderResponseDto OrderResponseGrpc_To_OrderResponseDto(
      OrderResponse orderResponse)
  {
    OrderResponseDto dto = new OrderResponseDto();
    dto.setOrderId(orderResponse.getId());
    dto.setUserId(orderResponse.getUserId());
    dto.setOfferId(orderResponse.getOfferId());
    dto.setNumberOfItems(orderResponse.getNumberOfItems());
    dto.setStatus(orderResponse.getStatus());

    dto.setOrderTime(DateTimeConverter.convertProtoTimestamp_To_LocalDateTime(orderResponse.getOrderTime()));
    dto.setPricePerItem(orderResponse.getPricePerItem());
    return dto;
  }
}
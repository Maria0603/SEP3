package com.example.server.converters;

import com.example.sep3.grpc.CustomerLocationRequest;
import com.example.server.dto.customer.CustomerLocationRequestDto;

public class CustomerDtoGrpcConverter
{


  public static CustomerLocationRequest generateCustomerLocationRequestFromCustomerLocationRequestDto(
      CustomerLocationRequestDto dto, String customerEmail)
  {
    return CustomerLocationRequest.newBuilder().setCustomerEmail(customerEmail)
        .setRadius(dto.getRadius()).setLatitude(dto.getLatitude())
        .setLongitude(dto.getLongitude()).build();
  }
}

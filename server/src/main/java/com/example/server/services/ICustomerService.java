package com.example.server.services;

import com.example.sep3.grpc.BusinessOnMap;
import com.example.sep3.grpc.BusinessesInRadiusResponse;
import com.example.server.dto.business.BusinessInRadiusResponseDto;
import com.example.server.dto.customer.CustomerLocationRequestResponseDto;

import java.util.ArrayList;
import java.util.List;

import static com.example.server.converters.BusinessDtoGrpcConverter.generateBusinessInRadiusResponseDtoFromBusinessOnMap;
import static com.example.server.converters.CustomerDtoGrpcConverter.generateCustomerLocationRequestFromCustomerLocationRequestDto;

public interface ICustomerService
{
  List<BusinessInRadiusResponseDto> updateCustomerLocation(
      CustomerLocationRequestResponseDto request, String userId);
  List<BusinessInRadiusResponseDto> getBusinessesInRadius(String userId);
  CustomerLocationRequestResponseDto getCustomerLocation(String userId);
}

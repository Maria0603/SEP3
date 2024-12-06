package com.example.server.services;

import com.example.server.dto.business.BusinessInRadiusResponseDto;
import com.example.server.dto.customer.CustomerLocationRequestDto;

import java.util.List;

public interface ICustomerService
{
  List<BusinessInRadiusResponseDto> updateCustomerLocation(
      CustomerLocationRequestDto request, String userId);
  List<BusinessInRadiusResponseDto> getBusinessesInRadius(String userId);
}

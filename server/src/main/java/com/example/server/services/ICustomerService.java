package com.example.server.services;

import com.example.server.dto.business.BusinessInRadiusResponseDto;
import com.example.server.dto.customer.CustomerLocationRequestResponseDto;
import com.example.server.dto.customer.CustomerResponseDto;
import com.example.server.dto.customer.CustomerUpdateRequestDto;

import java.util.List;

public interface ICustomerService
{
  List<BusinessInRadiusResponseDto> updateCustomerLocation(
      CustomerLocationRequestResponseDto request, String userId);
  List<BusinessInRadiusResponseDto> getBusinessesInRadius(String userId);
  CustomerLocationRequestResponseDto getCustomerLocation(String userId);
  CustomerResponseDto updateCustomerProfile(CustomerUpdateRequestDto updatedProfile);
  CustomerResponseDto getCustomerById(String id);
 
}

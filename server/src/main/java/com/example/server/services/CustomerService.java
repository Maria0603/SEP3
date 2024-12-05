package com.example.server.services;

import com.example.sep3.grpc.*;

import com.example.server.DataServerStub;
import com.example.server.dto.business.BusinessInRadiusResponseDto;
import com.example.server.dto.customer.CustomerLocationRequestDto;
import com.example.server.services.auxServices.ImageStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.server.converters.BusinessDtoGrpcConverter.generateBusinessInRadiusResponseDtoFromBusinessOnMap;
import static com.example.server.converters.CustomerDtoGrpcConverter.generateCustomerLocationRequestFromCustomerLocationRequestDto;

@Service public class CustomerService
{
  private final DataServerStub dataServerStub;

  @Autowired public CustomerService(DataServerStub dataServerStub)
  {
    this.dataServerStub = dataServerStub;
    System.out.println("CustomerService created");
  }

  public List<BusinessInRadiusResponseDto> updateCustomerLocation(
      CustomerLocationRequestDto request, String customerEmail)
  {
    BusinessesInRadiusResponse databaseResponse = dataServerStub.updateCustomerLocation(
        generateCustomerLocationRequestFromCustomerLocationRequestDto(request,
            customerEmail));

    List<BusinessInRadiusResponseDto> businessesInRadiusResponseDto = new ArrayList<>();

    for (BusinessOnMap business : databaseResponse.getBusinessesList())
    {
      BusinessInRadiusResponseDto dto = generateBusinessInRadiusResponseDtoFromBusinessOnMap(business);
      businessesInRadiusResponseDto.add(dto);
    }

    return businessesInRadiusResponseDto;
  }

  public List<BusinessInRadiusResponseDto> getBusinessesInRadius(String customerEmail)
  {
    BusinessesInRadiusResponse databaseResponse = dataServerStub.getBusinessesInRadius(
        BusinessesInRadiusRequest.newBuilder().setCustomerEmail(customerEmail).build());
    List<BusinessInRadiusResponseDto> businessesInRadiusResponseDto = new ArrayList<>();

    for (BusinessOnMap business : databaseResponse.getBusinessesList())
    {
      BusinessInRadiusResponseDto dto = generateBusinessInRadiusResponseDtoFromBusinessOnMap(business);
      businessesInRadiusResponseDto.add(dto);
    }

    return businessesInRadiusResponseDto;
  }

}
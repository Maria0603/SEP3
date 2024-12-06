package com.example.server.services.Implementations;

import com.example.sep3.grpc.*;

import com.example.server.DataServerStub;
import com.example.server.dto.business.BusinessInRadiusResponseDto;
import com.example.server.dto.customer.CustomerLocationRequestDto;
import com.example.server.services.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.server.converters.BusinessDtoGrpcConverter.generateBusinessInRadiusResponseDtoFromBusinessOnMap;
import static com.example.server.converters.CustomerDtoGrpcConverter.generateCustomerLocationRequestFromCustomerLocationRequestDto;

@Service public class CustomerService implements ICustomerService
{
  private final DataServerStub dataServerStub;

  @Autowired public CustomerService(DataServerStub dataServerStub)
  {
    this.dataServerStub = dataServerStub;
    System.out.println("CustomerService created");
  }

  @Override public List<BusinessInRadiusResponseDto> updateCustomerLocation(
      CustomerLocationRequestDto request, String userId)
  {
    BusinessesInRadiusResponse databaseResponse = dataServerStub.updateCustomerLocation(
        generateCustomerLocationRequestFromCustomerLocationRequestDto(request,
            userId));

    List<BusinessInRadiusResponseDto> businessesInRadiusResponseDto = new ArrayList<>();

    for (BusinessOnMap business : databaseResponse.getBusinessesList())
    {
      BusinessInRadiusResponseDto dto = generateBusinessInRadiusResponseDtoFromBusinessOnMap(business);
      businessesInRadiusResponseDto.add(dto);
    }

    return businessesInRadiusResponseDto;
  }

  @Override public List<BusinessInRadiusResponseDto> getBusinessesInRadius(
      String userId)
  {
    BusinessesInRadiusResponse databaseResponse = dataServerStub.getBusinessesInRadius(
        IdRequestResponse.newBuilder().setId(userId).build());
    List<BusinessInRadiusResponseDto> businessesInRadiusResponseDto = new ArrayList<>();

    for (BusinessOnMap business : databaseResponse.getBusinessesList())
    {
      BusinessInRadiusResponseDto dto = generateBusinessInRadiusResponseDtoFromBusinessOnMap(business);
      businessesInRadiusResponseDto.add(dto);
    }

    return businessesInRadiusResponseDto;
  }

}

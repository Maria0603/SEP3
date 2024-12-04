package com.example.server.services;

import com.example.sep3.grpc.*;

import com.example.server.DataServerStub;
import com.example.server.dto.business.BusinessesInRadiusRequestDto;
import com.example.server.dto.business.BusinessesInRadiusResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service public class CustomerService
{
  private final DataServerStub dataServerStub;
  private final ImageStorageService imageStorageService;

  @Autowired public CustomerService(DataServerStub dataServerStub,
      ImageStorageService imageStorageService)
  {
    this.dataServerStub = dataServerStub;
    this.imageStorageService = imageStorageService;
    System.out.println("CustomerService created");
  }

  //TODO: finish this
  public List<BusinessesInRadiusResponseDto> updateCustomerLocation(
      BusinessesInRadiusRequestDto request)
  {
    BusinessesInRadiusResponse databaseResponse = dataServerStub.updateCustomerLocation(
        generateBusinessesInRadiusRequestFromBusinessesInRadiusRequestDto(
            request));
    return null;
  }

  private BusinessesInRadiusRequest generateBusinessesInRadiusRequestFromBusinessesInRadiusRequestDto(
      BusinessesInRadiusRequestDto dto)
  {
    //TODO: extract customer's email
    return BusinessesInRadiusRequest.newBuilder().setRadius(dto.getRadius())
        .setLatitude(dto.getLatitude()).setLongitude(dto.getLongitude())
        .build();
  }
}

package com.example.server.services.Implementations;

import com.example.sep3.grpc.*;
import com.example.server.DataServerStub;
import com.example.server.converters.CustomerDtoGrpcConverter;
import com.example.server.dto.business.BusinessInRadiusResponseDto;
import com.example.server.dto.customer.CustomerLocationRequestResponseDto;
import com.example.server.dto.customer.CustomerResponseDto;
import com.example.server.dto.customer.CustomerUpdateRequestDto;
import com.example.server.services.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.server.converters.BusinessDtoGrpcConverter.generateBusinessInRadiusResponseDtoFromBusinessOnMap;
import static com.example.server.converters.CustomerDtoGrpcConverter.*;

@Service public class CustomerService implements ICustomerService
{
  private final DataServerStub dataServerStub;

  @Autowired public CustomerService(DataServerStub dataServerStub)
  {
    this.dataServerStub = dataServerStub;
    System.out.println("CustomerService created");
  }

  @Override public List<BusinessInRadiusResponseDto> updateCustomerLocation(
      CustomerLocationRequestResponseDto request, String userId)
  {
    BusinessesInRadiusResponse databaseResponse = dataServerStub.updateCustomerLocation(
        generateCustomerLocationRequestFromCustomerLocationRequestDto(request,
            userId));

    List<BusinessInRadiusResponseDto> businessesInRadiusResponseDto = new ArrayList<>();

    for (BusinessOnMap business : databaseResponse.getBusinessesList())
    {
      BusinessInRadiusResponseDto dto = generateBusinessInRadiusResponseDtoFromBusinessOnMap(
          business);
      businessesInRadiusResponseDto.add(dto);
    }

    return businessesInRadiusResponseDto;
  }

  @Override public CustomerLocationRequestResponseDto getCustomerLocation(
      String userId)
  {
    CustomerLocationRequest locationGrpc = dataServerStub.getCustomerLocation(
        IdRequestResponse.newBuilder().setId(userId).build());

    return CustomerLocationRequest_To_CustomerLocationRequestResponseDto(locationGrpc);
  }

  @Override public List<BusinessInRadiusResponseDto> getBusinessesInRadius(
      String userId)
  {
    BusinessesInRadiusResponse databaseResponse = dataServerStub.getBusinessesInRadius(
        IdRequestResponse.newBuilder().setId(userId).build());
    List<BusinessInRadiusResponseDto> businessesInRadiusResponseDto = new ArrayList<>();

    for (BusinessOnMap business : databaseResponse.getBusinessesList())
    {
      BusinessInRadiusResponseDto dto = generateBusinessInRadiusResponseDtoFromBusinessOnMap(
          business);
      businessesInRadiusResponseDto.add(dto);
    }

    return businessesInRadiusResponseDto;
  }

  @Override
  public CustomerResponseDto updateCustomerProfile(
      CustomerUpdateRequestDto updatedProfile) {
    System.out.println("updateCustomerProfile method called for ID: " + updatedProfile.getId());


    System.out.println("Updated Profile Details:");
    System.out.println("First Name: " + updatedProfile.getFirstName());
    System.out.println("Last Name: " + updatedProfile.getLastName());
    System.out.println("Email: " + updatedProfile.getEmail());
    System.out.println("Phone Number: " + updatedProfile.getPhoneNumber());

    CustomerUpdateRequest request = CustomerUpdateRequestDto_To_CustomerUpdateRequest(updatedProfile);

    try {
      CustomerResponse grpcResponse = dataServerStub.updateCustomerProfile(request);
      return CustomerDtoGrpcConverter.CustomerResponseGrpc_To_BusinessResponseDto(grpcResponse);
    } catch (Exception e) {
      System.out.println("Error updating customer profile: " + e.getMessage());
      // return false;
    }
    return null;
  }

  @Override public CustomerResponseDto getCustomerById(String id)
  {
    System.out.println("getCustomerId method called with id " + id);
    IdRequest request = IdRequest.newBuilder().setId(id).build();
    CustomerResponse response = dataServerStub.getCustomerById(request);
    System.out.println("Received response from dataServerStub: " + response);

    return CustomerDtoGrpcConverter.CustomerResponseGrpc_To_BusinessResponseDto(response);
  }

}

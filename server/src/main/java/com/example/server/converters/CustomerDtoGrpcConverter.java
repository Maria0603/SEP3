package com.example.server.converters;

import com.example.sep3.grpc.RegisterCustomerRequest;
import com.example.server.dto.customer.RegisterCustomerRequestDto;
import com.example.shared.model.UserRole;
import com.example.sep3.grpc.CustomerLocationRequest;
import com.example.server.dto.customer.CustomerLocationRequestResponseDto;

public class CustomerDtoGrpcConverter
{

  public static RegisterCustomerRequest RegisterCustomerRequestDto_To_RegisterCustomerRequest(
      RegisterCustomerRequestDto dto, String hashedPassword)
  {

    return RegisterCustomerRequest.newBuilder().setFirstName(dto.getFirstName())
        .setLastName(dto.getLastName()).setEmail(dto.getEmail())
        .setPhoneNumber(dto.getPhoneNumber()).setHashedPassword(hashedPassword)
        .setRole(UserRole.CUSTOMER.getRoleName()).build();
  }

  public static CustomerLocationRequest generateCustomerLocationRequestFromCustomerLocationRequestDto(
      CustomerLocationRequestResponseDto dto, String customerId)
  {
    System.out.println(
        "Customer id " + customerId + " is updating the location to "
            + dto.getLatitude() + " " + dto.getLongitude());
    return CustomerLocationRequest.newBuilder().setCustomerId(customerId)
        .setRadius(dto.getRadius()).setLatitude(dto.getLatitude())
        .setLongitude(dto.getLongitude()).build();
  }

  public static CustomerLocationRequestResponseDto CustomerLocationRequest_To_CustomerLocationRequestResponseDto(CustomerLocationRequest grpc)
  {
    CustomerLocationRequestResponseDto dto = new CustomerLocationRequestResponseDto();
    dto.setLatitude(grpc.getLatitude());
    dto.setLongitude(grpc.getLongitude());
    dto.setRadius(grpc.getRadius());
    return dto;
  }

}

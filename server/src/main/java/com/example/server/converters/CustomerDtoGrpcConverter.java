package com.example.server.converters;

import com.example.sep3.grpc.CustomerLocationRequest;
import com.example.sep3.grpc.CustomerResponse;
import com.example.sep3.grpc.CustomerUpdateRequest;
import com.example.sep3.grpc.RegisterCustomerRequest;
import com.example.server.dto.address.AddressDto;
import com.example.server.dto.customer.CustomerLocationRequestResponseDto;
import com.example.server.dto.customer.CustomerResponseDto;
import com.example.server.dto.customer.CustomerUpdateRequestDto;
import com.example.server.dto.customer.RegisterCustomerRequestDto;
import com.example.shared.model.UserRole;

public class CustomerDtoGrpcConverter
{
  public static CustomerResponseDto CustomerResponseGrpc_To_BusinessResponseDto(
      CustomerResponse response) {
   CustomerResponseDto dto = new CustomerResponseDto();
    AddressDto addressDto = new AddressDto();
    dto.setId(response.getId());
    dto.setEmail(response.getEmail());
    dto.setPhoneNumber(response.getPhoneNumber());
    dto.setFirstName(response.getFirstName());
    dto.setLastName(response.getLastName());
    dto.setRole(response.getRole());

    dto.setAddress(addressDto);

    return dto;
  }

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

  public static CustomerUpdateRequest CustomerResponseDto_To_CustomerUpdateRequest(CustomerResponseDto updatedProfile) {
    CustomerUpdateRequest.Builder requestBuilder = CustomerUpdateRequest.newBuilder();

    // Set fields only if they are non-null and non-empty
    if (updatedProfile.getFirstName() != null && !updatedProfile.getFirstName().isEmpty()) {
      requestBuilder.setFirstName(updatedProfile.getFirstName());
    }
    if (updatedProfile.getLastName() != null && !updatedProfile.getLastName().isEmpty()) {
      requestBuilder.setLastName(updatedProfile.getLastName());
    }

    if (updatedProfile.getEmail() != null && !updatedProfile.getEmail().isEmpty()) {
      requestBuilder.setEmail(updatedProfile.getEmail());
    }

    if (updatedProfile.getPhoneNumber() != null && !updatedProfile.getPhoneNumber().isEmpty()) {
      requestBuilder.setPhoneNumber(updatedProfile.getPhoneNumber());
    }

    return requestBuilder.build();
  }

  public static CustomerUpdateRequest CustomerUpdateRequestDto_To_CustomerUpdateRequest(
      CustomerUpdateRequestDto requestDto)
  {
    return CustomerUpdateRequest.newBuilder()
        .setId(requestDto.getId())
        .setFirstName(requestDto.getFirstName())
        .setLastName(requestDto.getLastName())
        .setEmail(requestDto.getEmail())
        .setPhoneNumber(requestDto.getPhoneNumber())

        .build();
  }

}

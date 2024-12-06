package com.example.server.converters;

import com.example.sep3.grpc.RegisterCustomerRequest;
import com.example.server.dto.customer.RegisterCustomerRequestDto;
import com.example.shared.model.UserRole;
import com.example.sep3.grpc.CustomerLocationRequest;
import com.example.server.dto.customer.CustomerLocationRequestDto;

public class CustomerDtoGrpcConverter {

    public static RegisterCustomerRequest RegisterCustomerRequestDto_To_RegisterCustomerRequest(
            RegisterCustomerRequestDto dto, String hashedPassword) {

        return RegisterCustomerRequest.newBuilder()
                .setFirstName(dto.getFirstName())
                .setLastName(dto.getLastName())
                .setEmail(dto.getEmail())
                .setPhoneNumber(dto.getPhoneNumber())
                .setHashedPassword(hashedPassword)
                .setAddress(AddressConverter.convertAddressDtoToGrpcAddress(dto.getAddress()))
                .setRole(UserRole.CUSTOMER.getRoleName())
                .build();
    }

    public static CustomerLocationRequest generateCustomerLocationRequestFromCustomerLocationRequestDto(
      CustomerLocationRequestDto dto, String customerId)
  {
    return CustomerLocationRequest.newBuilder().setCustomerId(customerId)
        .setRadius(dto.getRadius()).setLatitude(dto.getLatitude())
        .setLongitude(dto.getLongitude()).build();
  }


}

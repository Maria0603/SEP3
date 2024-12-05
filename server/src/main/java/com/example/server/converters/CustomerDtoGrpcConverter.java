package com.example.server.converters;

import com.example.sep3.grpc.RegisterCustomerRequest;
import com.example.server.dto.customer.RegisterCustomerRequestDto;
import com.example.shared.model.UserRole;

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
}

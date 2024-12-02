package com.example.server.converters;

import com.example.sep3.grpc.RegisterBusinessRequest;
import com.example.server.dto.business.RegisterBusinessRequestDto;
import com.example.shared.model.UserRole;

public class BusinessDtoGrpcConverter
{
  public static RegisterBusinessRequest RegisterBusinessRequestDto_To_RegisterBusinessRequest(
      RegisterBusinessRequestDto dto, String logoPath, String hashedPassword)
  {
    RegisterBusinessRequest.Builder builder = RegisterBusinessRequest.newBuilder();
    return RegisterBusinessRequest.newBuilder()
        .setBusinessName(dto.getBusinessName()).setCvr(dto.getCvr()).setAddress(
            AddressConverter.convertAddressDtoToGrpcAddress(dto.getAddress()))
        .setEmail(dto.getEmail()).setHashedPassword(hashedPassword)
        .setLogoPath(logoPath).setPhoneNumber(dto.getPhoneNumber())
        .setRole(UserRole.BUSINESS.getRoleName()).build();
  }


}

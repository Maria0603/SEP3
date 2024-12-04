package com.example.server.converters;

import com.example.sep3.grpc.BusinessesInRadiusRequest;
import com.example.sep3.grpc.RegisterBusinessRequest;
import com.example.server.dto.business.BusinessesInRadiusRequestDto;
import com.example.server.dto.business.RegisterBusinessRequestDto;
import com.example.shared.model.UserRole;

public class BusinessDtoGrpcConverter
{
  public static RegisterBusinessRequest RegisterBusinessRequestDto_To_RegisterBusinessRequest(
      RegisterBusinessRequestDto dto, String logoPath, String hashedPassword)
  {
    return RegisterBusinessRequest.newBuilder()
        .setBusinessName(dto.getBusinessName()).setCvr(dto.getCvr()).setAddress(
            AddressConverter.convertAddressDtoToGrpcAddress(dto.getAddress()))
        .setEmail(dto.getEmail()).setHashedPassword(hashedPassword)
        .setLogoPath(logoPath).setPhoneNumber(dto.getPhoneNumber())
        .setRole(UserRole.BUSINESS.getRoleName()).build();
  }

  public static BusinessesInRadiusRequest generateBusinessesInRadiusRequest(
      BusinessesInRadiusRequestDto dto)
  {
    return BusinessesInRadiusRequest.newBuilder().setRadius(dto.getRadius())
        .setLatitude(dto.getLatitude()).setLongitude(dto.getLongitude())
        .build();
  }

}

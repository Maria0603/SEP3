package com.example.server.converters;

import com.example.sep3.grpc.BusinessResponse;
import com.example.sep3.grpc.RegisterBusinessRequest;
import com.example.server.dto.address.AddressDto;
import com.example.server.dto.business.BusinessResponseDto;
import com.example.sep3.grpc.BusinessOnMap;
import com.example.sep3.grpc.BusinessesInRadiusResponse;
import com.example.sep3.grpc.RegisterBusinessRequest;
import com.example.server.dto.business.BusinessInRadiusResponseDto;
import com.example.server.dto.business.RegisterBusinessRequestDto;
import com.example.shared.model.UserRole;

import java.util.ArrayList;
import java.util.List;

public class BusinessDtoGrpcConverter
{
  public static RegisterBusinessRequest RegisterBusinessRequestDto_To_RegisterBusinessRequest(
      RegisterBusinessRequestDto dto, String logoPath, double latitude,
      double longitude, String hashedPassword)
  {
    return RegisterBusinessRequest.newBuilder()
        .setBusinessName(dto.getBusinessName()).setCvr(dto.getCvr()).setAddress(
            AddressConverter.convertAddressDtoToGrpcAddress(dto.getAddress()))
        .setEmail(dto.getEmail()).setHashedPassword(hashedPassword)
        .setLogoPath(logoPath).setPhoneNumber(dto.getPhoneNumber())
        .setLatitude(latitude).setLongitude(longitude)
        .setRole(UserRole.BUSINESS.getRoleName()).build();
  }

  // This is to send to client, **without hashed password**
  public static BusinessResponseDto BusinessResponseGrpc_To_BusinessResponseDto(
          BusinessResponse response)
  {
    BusinessResponseDto dto = new BusinessResponseDto();
    AddressDto addressDto = new AddressDto();


    dto.setId(dto.getId());
    dto.setEmail(response.getEmail());
    dto.setPhoneNumber(response.getPhoneNumber());
    dto.setLogo_path(response.getLogoPath());
    dto.setCvr(response.getCvr());
    dto.setBusinessName(response.getBusinessName());
    dto.setRole(response.getRole());

    addressDto.setStreet(response.getAddress().getStreet());
    addressDto.setNumber(response.getAddress().getNumber());
    addressDto.setCity(response.getAddress().getCity());
    addressDto.setCounty(response.getAddress().getCounty());
    addressDto.setState(response.getAddress().getState());
    addressDto.setCountry(response.getAddress().getCountry());
    addressDto.setPostalCode(response.getAddress().getPostalCode());
    dto.setAddress(addressDto);

    return dto;
  }
  public static BusinessInRadiusResponseDto generateBusinessInRadiusResponseDtoFromBusinessOnMap(
      BusinessOnMap business)
  {
    BusinessInRadiusResponseDto dto = new BusinessInRadiusResponseDto();
    dto.setLatitude(business.getLatitude());
    dto.setLongitude(business.getLongitude());
    dto.setBusinessId(business.getBusinessId());
    dto.setBusinessName(business.getBusinessName());
    dto.setBusinessEmail(business.getBusinessEmail());
    dto.setLogoPath(business.getLogoPath());
    return dto;
  }


}

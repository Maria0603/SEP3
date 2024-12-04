package com.example.server.converters;

import com.example.sep3.grpc.BusinessResponse;
import com.example.sep3.grpc.RegisterBusinessRequest;
import com.example.server.dto.address.AddressDto;
import com.example.server.dto.business.BusinessResponseDto;
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

}

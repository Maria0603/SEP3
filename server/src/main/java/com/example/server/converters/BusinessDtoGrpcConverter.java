package com.example.server.converters;

import com.example.sep3.grpc.BusinessOnMap;
import com.example.sep3.grpc.BusinessResponse;
import com.example.sep3.grpc.BusinessUpdateRequest;
import com.example.sep3.grpc.RegisterBusinessRequest;
import com.example.server.dto.address.AddressDto;
import com.example.server.dto.business.BusinessInRadiusResponseDto;
import com.example.server.dto.business.BusinessResponseDto;
import com.example.server.dto.business.BusinessUpdateRequestDto;
import com.example.server.dto.business.RegisterBusinessRequestDto;
import com.example.server.dto.offer.LocationDto;
import com.example.shared.model.UserRole;

public class BusinessDtoGrpcConverter {

  // Converts RegisterBusinessRequestDto to RegisterBusinessRequest
  public static RegisterBusinessRequest RegisterBusinessRequestDto_To_RegisterBusinessRequest(
      RegisterBusinessRequestDto dto, String logoPath, double latitude,
      double longitude, String hashedPassword) {
    return RegisterBusinessRequest.newBuilder()
        .setBusinessName(dto.getBusinessName()).setCvr(dto.getCvr()).setAddress(
            AddressConverter.convertAddressDtoToGrpcAddress(dto.getAddress()))
        .setEmail(dto.getEmail()).setHashedPassword(hashedPassword)
        .setLogoPath(logoPath).setPhoneNumber(dto.getPhoneNumber())
        .setLatitude(latitude).setLongitude(longitude)
        .setRole(UserRole.BUSINESS.getRoleName()).build();
  }




  // Converts BusinessResponse (gRPC) to BusinessResponseDto (DTO)
  public static BusinessResponseDto BusinessResponseGrpc_To_BusinessResponseDto(
          BusinessResponse response) {
    BusinessResponseDto dto = new BusinessResponseDto();
    AddressDto addressDto = new AddressDto();
    LocationDto locationDto = new LocationDto();

    // Map basic fields
    dto.setId(response.getId());
    dto.setEmail(response.getEmail());
    dto.setPhoneNumber(response.getPhoneNumber());
    dto.setLogo_path(response.getLogoPath());
    dto.setCvr(response.getCvr());
    dto.setBusinessName(response.getBusinessName());
    dto.setRole(response.getRole());

    // Map address
    addressDto.setStreet(response.getAddress().getStreet());
    addressDto.setNumber(response.getAddress().getNumber());
    addressDto.setCity(response.getAddress().getCity());
    addressDto.setCounty(response.getAddress().getCounty());
    addressDto.setState(response.getAddress().getState());
    addressDto.setCountry(response.getAddress().getCountry());
    addressDto.setPostalCode(response.getAddress().getPostalCode());
    dto.setAddress(addressDto);

    dto.setLatitude(response.getLatitude());
    dto.setLongitude(response.getLongitude());

    return dto;
  }

  // Converts BusinessOnMap (gRPC) to BusinessInRadiusResponseDto (DTO)
  public static BusinessInRadiusResponseDto generateBusinessInRadiusResponseDtoFromBusinessOnMap(
      BusinessOnMap business) {
    BusinessInRadiusResponseDto dto = new BusinessInRadiusResponseDto();
    dto.setLatitude(business.getLatitude());
    dto.setLongitude(business.getLongitude());
    dto.setBusinessId(business.getBusinessId());
    dto.setBusinessName(business.getBusinessName());
    dto.setBusinessEmail(business.getBusinessEmail());
    dto.setLogoPath(business.getLogoPath());
    return dto;
  }

  // Converts BusinessResponseDto (DTO) to BusinessUpdateRequest (gRPC)
  public static BusinessUpdateRequest BusinessResponseDto_To_BusinessUpdateRequest(
      BusinessResponseDto updatedProfile) {
    BusinessUpdateRequest.Builder requestBuilder = BusinessUpdateRequest.newBuilder();

    // Set fields only if they are non-null and non-empty
    if (updatedProfile.getBusinessName() != null && !updatedProfile.getBusinessName().isEmpty()) {
      requestBuilder.setBusinessName(updatedProfile.getBusinessName());
    }

    if (updatedProfile.getEmail() != null && !updatedProfile.getEmail().isEmpty()) {
      requestBuilder.setEmail(updatedProfile.getEmail());
    }

    if (updatedProfile.getPhoneNumber() != null && !updatedProfile.getPhoneNumber().isEmpty()) {
      requestBuilder.setPhoneNumber(updatedProfile.getPhoneNumber());
    }
/*
    if (updatedProfile.getLogo_path() != null && !updatedProfile.getLogo_path().isEmpty()) {
      requestBuilder.setLogoPath(updatedProfile.getLogo_path());
    }*/

    // Add other fields if required
    return requestBuilder.build();
  }

  public static BusinessUpdateRequest BusinessUpdateRequestDto_To_BusinessUpdateRequest(
      BusinessUpdateRequestDto requestDto)
  {
    return BusinessUpdateRequest.newBuilder()
        .setId(requestDto.getId())
        .setBusinessName(requestDto.getBusinessName())
        .setEmail(requestDto.getEmail())
        .setPhoneNumber(requestDto.getPhoneNumber())
        //.setLogoPath(requestDto.getLogoPath())
        .build();
  }
}

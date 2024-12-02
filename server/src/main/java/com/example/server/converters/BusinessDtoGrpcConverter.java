package com.example.server.converters;

import com.example.sep3.grpc.RegisterBusinessRequest;
import com.example.sep3.grpc.RegisterBusinessResponse;
import com.example.server.dto.auth.CredentialsResponseDto;
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

  public static CredentialsResponseDto RegisterBusinessResponse_To_CredentialsResponseDto(
      RegisterBusinessResponse response, String jwt, String refreshToken, java.util.Date expiresAt)
  {
    CredentialsResponseDto dto = new CredentialsResponseDto();
    dto.setId(response.getBusinessId());
    dto.setRole(response.getRole());
    dto.setToken(jwt);
    dto.setRefreshToken(refreshToken);
    dto.setExpiresAt(expiresAt);

    return dto;
  }

}

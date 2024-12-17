package com.example.server.services;

import com.example.server.dto.auth.CredentialsResponseDto;
import com.example.server.dto.auth.LoginRequestDto;
import com.example.server.dto.auth.RefreshTokenRequest;
import com.example.server.dto.business.RegisterBusinessRequestDto;
import com.example.server.dto.customer.RegisterCustomerRequestDto;

import java.io.IOException;

public interface IAuthService
{
  CredentialsResponseDto registerBusiness(
      RegisterBusinessRequestDto registrationRequestDto);
  CredentialsResponseDto registerCustomer(
      RegisterCustomerRequestDto registrationRequestDto);
  CredentialsResponseDto logIn(LoginRequestDto loginRequest);
  CredentialsResponseDto refreshToken(RefreshTokenRequest refreshTokenRequest);

}

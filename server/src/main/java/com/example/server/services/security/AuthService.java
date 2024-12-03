package com.example.server.services.security;

import com.example.sep3.grpc.*;
import com.example.server.DataServerStub;
import com.example.server.converters.BusinessDtoGrpcConverter;
import com.example.server.dto.auth.CredentialsResponseDto;
import com.example.server.dto.auth.LoginRequestDto;
import com.example.server.dto.auth.RefreshTokenRequest;
import com.example.server.dto.business.RegisterBusinessRequestDto;
import com.example.server.services.ImageStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

import java.util.HashMap;

@Service public class AuthService
{

  private final DataServerStub dataServerStub;
  private final ImageStorageService imageStorageService;

  private final JWTUtils jwtUtils;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final UserDetailsService userDetailsService;

  @Autowired AuthService(DataServerStub dataServerStub,
      ImageStorageService imageService, JWTUtils jwtUtils,
      PasswordEncoder passwordEncoder,
      AuthenticationManager authenticationManager,
      UserDetailsService userDetailsService)
  {
    this.dataServerStub = dataServerStub;
    this.imageStorageService = imageService;

    this.jwtUtils = jwtUtils;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.userDetailsService = userDetailsService;

    System.out.println("AuthService created");
  }

  //TODO: chop this method in smaller methods
  public CredentialsResponseDto registerBusiness(
      RegisterBusinessRequestDto registrationRequestDto)
  {

    System.out.println("Request for register business in service");
    String logoPath = null;
    try
    {
      logoPath = imageStorageService.getBaseDirectory()
          + imageStorageService.saveImage(registrationRequestDto.getLogo());


      //Transform the dto to grpc message
      RegisterBusinessRequest registerBusinessRequest = BusinessDtoGrpcConverter.RegisterBusinessRequestDto_To_RegisterBusinessRequest(
          registrationRequestDto, logoPath,
          passwordEncoder.encode(registrationRequestDto.getPassword()));

      //Send the request to the data server
      dataServerStub.registerBusiness(registerBusinessRequest);

      UserDetails userDetails = userDetailsService.loadUserByUsername(
          registrationRequestDto.getEmail());

      System.out.println("USER IS: " + userDetails.getUsername());

      //Generate the token
      String jwt = jwtUtils.generateToken(userDetails);
      String refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(),
          userDetails);

      System.out.println("Token successfully generated");
      //Return the credentials
      return buildCredentialsResponseDto(jwt, refreshToken);
    }
    catch (IOException e)
    {
      e.printStackTrace();
      if (logoPath != null)
        imageStorageService.deleteImage(logoPath); //rollback
      throw new IllegalArgumentException("Failed to save the image");
    }

  }

  public CredentialsResponseDto logIn(LoginRequestDto loginRequest)
  {
    try
    {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
              loginRequest.getPassword()));

      UserDetails userDetails = userDetailsService.loadUserByUsername(
          loginRequest.getEmail());

      /*BusinessResponse user = dataServerStub.getBusinessByEmail(
          BusinessByEmailRequest.newBuilder().setEmail(loginRequest.getEmail())
              .build());*/
      System.out.println("USER IS: " + userDetails.getUsername());
      String jwt = jwtUtils.generateToken(userDetails);
      String refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(),
          userDetails);

      return buildCredentialsResponseDto(jwt, refreshToken);
    }
    catch (Exception e)
    {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  public CredentialsResponseDto refreshToken(
      RefreshTokenRequest refreshTokenRequest)
  {
    String email = jwtUtils.extractUsername(refreshTokenRequest.getToken());

    UserDetails business = userDetailsService.loadUserByUsername(
        email); //Remember, the username is the email

    if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), business))
    {
      var jwt = jwtUtils.generateToken(business);
      return buildCredentialsResponseDto(jwt, refreshTokenRequest.getToken());
    }
    throw new IllegalArgumentException("Invalid refresh token");
  }

  private CredentialsResponseDto buildCredentialsResponseDto(String token,
      String refreshToken)
  {
    CredentialsResponseDto dto = new CredentialsResponseDto();
    dto.setToken(token);
    dto.setRefreshToken(refreshToken);
    return dto;
  }

}

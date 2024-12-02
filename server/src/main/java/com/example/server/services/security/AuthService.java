package com.example.server.services.security;

import com.example.sep3.grpc.*;
import com.example.server.DataServerStub;
import com.example.server.converters.AddressConverter;
import com.example.server.converters.BusinessDtoGrpcConverter;
import com.example.server.dto.auth.CredentialsResponseDto;
import com.example.server.dto.business.RegisterBusinessRequestDto;
import com.example.server.services.ImageStorageService;
import com.example.shared.dao.usersDao.BusinessDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import static com.example.server.converters.BusinessDtoGrpcConverter.RegisterBusinessResponse_To_CredentialsResponseDto;

@Service public class AuthService
{

  private final DataServerStub dataServerStub;
  private final ImageStorageService imageStorageService;

  private final JWTUtils jwtUtils;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  @Autowired AuthService(DataServerStub dataServerStub,
      ImageStorageService imageService, JWTUtils jwtUtils,
      PasswordEncoder passwordEncoder,
      AuthenticationManager authenticationManager)
  {
    this.dataServerStub = dataServerStub;
    this.imageStorageService = imageService;

    this.jwtUtils = jwtUtils;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;

    System.out.println("AuthService created");
  }

  //TODO: chop this method in smaller methods
  public CredentialsResponseDto registerBusiness(
      RegisterBusinessRequestDto registrationRequestDto)
  {

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
      RegisterBusinessResponse response = dataServerStub.registerBusiness(
          registerBusinessRequest);

      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              registrationRequestDto.getEmail(),
              registrationRequestDto.getPassword()));

      //Extract the business from database
      BusinessResponse businessResponse = dataServerStub.getBusinessByEmail(
          EmailRequest.newBuilder().setEmail(registrationRequestDto.getEmail())
              .build());

      //Convert to dao, because dao implements UserDetails interface
      BusinessDao business = generateBusinessDaoFromBusinessResponse(
          businessResponse);
      System.out.println("USER IS: " + business);

      //Generate the token
      String jwt = jwtUtils.generateToken(business);
      String refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(),
          business);

      System.out.println("Token successfully generated");
      //Return the credentials
      return RegisterBusinessResponse_To_CredentialsResponseDto(
          response, jwt, refreshToken,
          new Date(System.currentTimeMillis() + JWTUtils.getExpirationTime()));
    }
    catch (IOException e)
    {
      e.printStackTrace();
      if (logoPath != null)
        imageStorageService.deleteImage(logoPath); //rollback
      throw new IllegalArgumentException("Failed to save the image");
    }

  }

  private BusinessDao generateBusinessDaoFromBusinessResponse(
      BusinessResponse response)
  {
    BusinessDao dao = new BusinessDao();
    dao.setId(response.getId());
    dao.setCvr(response.getCvr());
    dao.setEmail(response.getEmail());
    dao.setAddress(
        AddressConverter.convertGrpcAddressToAddressDao(response.getAddress()));
    dao.setBusinessName(response.getBusinessName());
    dao.setPhoneNumber(response.getPhoneNumber());
    dao.setRole(response.getRole());
    dao.setLogoPath(response.getLogoPath());
    //TODO: not sure if i have to pass the raw password or the encoded one; I'll test
    dao.setPassword(response.getHashedPassword());
    return dao;
  }
/*
  public CredentialsResponseDto logIn(LoginBusinessRequest loginRequest)
  {
    ReqRes response = new ReqRes();

    try
    {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(signinRequest.getEmail(),
              signinRequest.getPassword()));
      var user = ourUserRepo.findByEmail(signinRequest.getEmail())
          .orElseThrow();
      System.out.println("USER IS: " + user);
      var jwt = jwtUtils.generateToken(user);
      var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
      response.setStatusCode(200);
      response.setToken(jwt);
      response.setRefreshToken(refreshToken);
      response.setExpirationTime("24Hr");
      response.setMessage("Successfully Signed In");
    }
    catch (Exception e)
    {
      response.setStatusCode(500);
      response.setError(e.getMessage());
    }
    return response;
  }

  public ReqRes refreshToken(ReqRes refreshTokenReqiest)
  {
    ReqRes response = new ReqRes();
    String ourEmail = jwtUtils.extractUsername(refreshTokenReqiest.getToken());
    OurUsers users = ourUserRepo.findByEmail(ourEmail).orElseThrow();
    if (jwtUtils.isTokenValid(refreshTokenReqiest.getToken(), users))
    {
      var jwt = jwtUtils.generateToken(users);
      response.setStatusCode(200);
      response.setToken(jwt);
      response.setRefreshToken(refreshTokenReqiest.getToken());
      response.setExpirationTime("24Hr");
      response.setMessage("Successfully Refreshed Token");
    }
    response.setStatusCode(500);
    return response;
  }*/
}

package com.example.server.services;

import com.example.sep3.grpc.RegisterBusinessResponse;
import com.example.server.DataServerStub;
import com.example.server.auth.PasswordHandler;
import com.example.server.converters.BusinessDtoGrpcConverter;
import com.example.server.dto.business.RegisterBusinessRequestDto;
import com.example.server.dto.business.JwtTokenPayloadDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.example.server.converters.OfferDtoGrpcConverter.SaveOfferResponseGrpc_To_OfferResponseDto;

@Service public class AuthService
{
  private final DataServerStub dataServerStub;
  private final ImageStorageService imageStorageService;

  @Autowired public AuthService(DataServerStub dataServerStub,
      ImageStorageService imageStorageService)
  {
    this.dataServerStub = dataServerStub;
    this.imageStorageService = imageStorageService;
    System.out.println("AuthService created");
  }

  //not finished
  public JwtTokenPayloadDto registerBusiness(
      RegisterBusinessRequestDto dto)
  {
    String logoPath = null;
    try
    {
      logoPath = imageStorageService.getBaseDirectory()
          + imageStorageService.saveImage(dto.getLogo());

      String hashedPassword = PasswordHandler.hashPassword(dto.getPassword());

      RegisterBusinessResponse grpcResponse = dataServerStub.registerBusiness(
          BusinessDtoGrpcConverter.RegisterBusinessRequestDto_To_RegisterBusinessRequest(
              dto, logoPath, hashedPassword));
      JwtTokenPayloadDto response = new JwtTokenPayloadDto();

      //Return the offer as dto
      return null;
    }
    catch (IOException e)
    {
      e.printStackTrace();
      if (logoPath != null)
        imageStorageService.deleteImage(logoPath); //rollback
      throw new IllegalArgumentException("Failed to save the image");
    }
  }
}

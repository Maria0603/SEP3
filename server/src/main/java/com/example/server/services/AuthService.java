//package com.example.server.services;
//
//import com.example.sep3.grpc.BusinessServiceGrpc;
//import com.example.sep3.grpc.LoginBusinessRequest;
//import com.example.sep3.grpc.LoginBusinessResponse;
//import com.example.server.DataServerStub;
//import com.example.server.dto.auth.LoginBusinessRequestDto;
//import io.grpc.stub.StreamObserver;
//import org.springframework.stereotype.Service;
//import com.example.sep3.grpc.RegisterBusinessResponse;
//import com.example.server.DataServerStub;
//import com.example.server.auth.PasswordHandler;
//import com.example.server.converters.BusinessDtoGrpcConverter;
//import com.example.server.dto.business.RegisterBusinessRequestDto;
//import com.example.server.dto.business.JwtTokenPayloadDto;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//
//import static com.example.server.converters.OfferDtoGrpcConverter.SaveOfferResponseGrpc_To_OfferResponseDto;
//
//@Service public class AuthService extends BusinessServiceGrpc.BusinessServiceImplBase
//{
//  private final DataServerStub dataServerStub;
//  private final ImageStorageService imageStorageService;
//
//  @Autowired public AuthService(DataServerStub dataServerStub,
//      ImageStorageService imageStorageService)
//  {
//    this.dataServerStub = dataServerStub;
//    this.imageStorageService = imageStorageService;
//    System.out.println("AuthService created");
//  }
//
//  //not finished
//  public JwtTokenPayloadDto registerBusiness(
//      RegisterBusinessRequestDto dto)
//  {
//    String logoPath = null;
//    try
//    {
//      logoPath = imageStorageService.getBaseDirectory()
//          + imageStorageService.saveImage(dto.getLogo());
//
//      String hashedPassword = PasswordHandler.hashPassword(dto.getPassword());
//
//      RegisterBusinessResponse grpcResponse = dataServerStub.registerBusiness(
//          BusinessDtoGrpcConverter.RegisterBusinessRequestDto_To_RegisterBusinessRequest(
//              dto, logoPath, hashedPassword));
//      JwtTokenPayloadDto response = new JwtTokenPayloadDto();
//
//      //Return the offer as dto
//      return null;
//    }
//    catch (IOException e)
//    {
//      e.printStackTrace();
//      if (logoPath != null)
//        imageStorageService.deleteImage(logoPath); //rollback
//      throw new IllegalArgumentException("Failed to save the image");
//    }
//  }
//
//
//    public void loginBusiness(LoginBusinessRequestDto request) {
//        try {
//            // Validate the request (basic example)
//            if (request.getEmail().isEmpty() || request.getPassword().isEmpty()) {
//                responseObserver.onError(new IllegalArgumentException("Email and password must not be empty."));
//                return;
//            }
//
//            // Example of interacting with the stub to retrieve data (replace with your actual logic)
//            LoginBusinessResponse businessResponse = dataServerStub.findBusinessByEmail(request.getEmail());
//            if (businessResponse.getBusinessId() == null) {
//                responseObserver.onError(new IllegalArgumentException("Invalid email or password."));
//                return;
//            }
//
//            if (!PasswordHandler.matches(request.getPassword(), businessResponse.getHashedPassword())) {
//                responseObserver.onError(new IllegalArgumentException("Invalid password."));
//                return;
//            }
//
//            // Generate JWT token (replace with actual implementation)
//            String jwtToken = generateJwtToken(businessResponse.getBusinessId());
//
//            LoginBusinessResponse response = LoginBusinessResponse.newBuilder()
//                    .setBusinessId(businessResponse.getBusinessId())
//                    .setJwtToken(jwtToken)
//                    .setMessage("Login successful.")
//                    .build();
//
//            // Send the response
//            responseObserver.onNext(response);
//            responseObserver.onCompleted();
//        } catch (Exception e) {
//            // Handle any exceptions and notify the client
//            responseObserver.onError(e);
//        }
//    }
//
//}

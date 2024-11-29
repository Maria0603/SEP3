package com.example.data_server.service;

import com.example.data_server.repository.BusinessRepository;
import com.example.data_server.utility.AddressConverter;
import com.example.sep3.grpc.*;
import com.example.shared.dao.BusinessDao;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService public class BusinessServiceImpl
    extends AuthServiceGrpc.AuthServiceImplBase
{
  private final BusinessRepository businessRepository;

  @Autowired public BusinessServiceImpl(BusinessRepository businessRepository)
  {
    this.businessRepository = businessRepository;
    System.out.println("AuthServiceImpl created");

  }

  @Override public void registerBusiness(RegisterBusinessRequest request,
      StreamObserver<RegisterBusinessResponse> responseObserver)
  {
    System.out.println("Request for register business.");

    // Prepare to save the business details in database
    BusinessDao business = generateBusinessDaoFromRegisterBusinessRequest(
        request);

    BusinessDao createdBusiness = businessRepository.save(business);

    RegisterBusinessResponse response = buildRegisterBusinessResponse(
        createdBusiness);

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  private BusinessDao generateBusinessDaoFromRegisterBusinessRequest(
      RegisterBusinessRequest request)
  {
    BusinessDao business = new BusinessDao();
    business.setBusinessName(request.getBusinessName());
    business.setCvr(request.getCvr());
    business.setEmail(request.getEmail());
    business.setHashedPassword(request.getHashedPassword());
    business.setPhoneNumber(request.getPhoneNumber());
    business.setLogoPath(request.getLogoPath());
    business.setAddress(
        AddressConverter.convertGrpcAddressToAddressDao(request.getAddress()));
    return business;
  }

  private RegisterBusinessResponse buildRegisterBusinessResponse(
      BusinessDao business)
  {
    return RegisterBusinessResponse.newBuilder().setBusinessId(business.getId())
        .setEmail(business.getEmail()).build();
  }
}

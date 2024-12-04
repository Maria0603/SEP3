package com.example.data_server.service;

import com.example.data_server.repository.BusinessRepository;
import com.example.shared.converters.AddressConverter;
import com.example.sep3.grpc.*;
import com.example.shared.dao.usersDao.BusinessDao;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@GrpcService public class BusinessServiceImpl
    extends BusinessServiceGrpc.BusinessServiceImplBase
{
  private final BusinessRepository businessRepository;

  @Autowired public BusinessServiceImpl(BusinessRepository businessRepository)
  {
    this.businessRepository = businessRepository;
    System.out.println("AuthServiceImpl created");

  }

  @Override public void registerBusiness(RegisterBusinessRequest request,
      StreamObserver<EmptyMessage> responseObserver)
  {
    System.out.println("Request for register business.");

    // Prepare to save the business details in database
    BusinessDao business = generateBusinessDaoFromRegisterBusinessRequest(
        request);

    businessRepository.save(business);

    EmptyMessage response = EmptyMessage.newBuilder().build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override public void getBusinessByEmail(BusinessByEmailRequest request,
      StreamObserver<BusinessResponse> responseObserver)
  {
    Optional<BusinessDao> business = businessRepository.findByEmail(
        request.getEmail());
    if (business.isPresent())
    {
      BusinessResponse response = buildBusinessResponse(business.get());
      responseObserver.onNext(response);
      responseObserver.onCompleted();
      return;
    }

    throw new IllegalArgumentException("Business not found");
  }

  private BusinessResponse buildBusinessResponse(BusinessDao business)
  {
    return BusinessResponse.newBuilder()
        .setBusinessName(business.getBusinessName()).setAddress(
            AddressConverter.convertAddressDaoToGrpcAddress(
                business.getAddress())).setEmail(business.getEmail())
        .setCvr(business.getCvr()).setHashedPassword(business.getPassword())
        .setId(business.getId()).setLogoPath(business.getLogoPath())
        .setPhoneNumber(business.getPhoneNumber()).setRole(business.getRole())
        .build();
  }

  private BusinessDao generateBusinessDaoFromRegisterBusinessRequest(
      RegisterBusinessRequest request)
  {
    BusinessDao business = new BusinessDao();
    business.setBusinessName(request.getBusinessName());
    business.setCvr(request.getCvr());
    business.setEmail(request.getEmail());
    business.setPassword(request.getHashedPassword());
    business.setPhoneNumber(request.getPhoneNumber());
    business.setLogoPath(request.getLogoPath());
    business.setRole(request.getRole());
    business.setAddress(
        AddressConverter.convertGrpcAddressToAddressDao(request.getAddress()));
    return business;
  }

}

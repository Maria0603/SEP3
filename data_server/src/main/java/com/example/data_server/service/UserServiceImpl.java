package com.example.data_server.service;

import com.example.data_server.repository.BusinessRepository;
import com.example.data_server.repository.CustomerRepository;
import com.example.shared.dao.usersDao.BusinessDao;
import com.example.shared.dao.usersDao.CustomerDao;
import com.example.sep3.grpc.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@GrpcService public class UserServiceImpl
    extends UserServiceGrpc.UserServiceImplBase
{

  private final BusinessRepository businessRepository;
  private final CustomerRepository customerRepository;

  @Autowired public UserServiceImpl(BusinessRepository businessRepository,
      CustomerRepository customerRepository)
  {
    this.businessRepository = businessRepository;
    this.customerRepository = customerRepository;
  }

  @Override public void getUserByEmail(UserByEmailRequest request,
      StreamObserver<UserResponse> responseObserver)
  {
    String email = request.getEmail();

    // Check BusinessRepository
    Optional<BusinessDao> business = businessRepository.findByEmail(email);
    if (business.isPresent())
    {
      UserResponse response = buildUserResponseFromBusiness(business.get());
      responseObserver.onNext(response);
      responseObserver.onCompleted();
      return;
    }

    // Check CustomerRepository
    Optional<CustomerDao> customer = customerRepository.findByEmail(email);
    if (customer.isPresent())
    {
      UserResponse response = buildUserResponseFromCustomer(customer.get());
      responseObserver.onNext(response);
      responseObserver.onCompleted();
      return;
    }

    // If user not found in both repositories, throw an exception
    responseObserver.onError(
        new IllegalArgumentException("User not found with email: " + email));
  }

  private UserResponse buildUserResponseFromBusiness(BusinessDao business)
  {
    return UserResponse.newBuilder().setId(business.getId())
        .setEmail(business.getEmail()).setHashedPassword(business.getPassword())
        .setRole(business.getRole()).build();
  }

  private UserResponse buildUserResponseFromCustomer(CustomerDao customer)
  {
    return UserResponse.newBuilder().setId(customer.getId())
        .setEmail(customer.getEmail()).setHashedPassword(customer.getPassword())
        .setRole(customer.getRole()).build();
  }
}

package com.example.data_server.service;

import com.example.data_server.converters.CustomerEntityGrpcConverter;
import com.example.data_server.repository.BusinessRepository;
import com.example.data_server.repository.CustomerRepository;
import com.example.data_server.utility.GeoUtils;
import com.example.sep3.grpc.*;
import com.example.shared.entities.usersEntities.Business;
import com.example.shared.entities.usersEntities.Customer;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static com.example.data_server.converters.BusinessEntityGrpcConverter.buildBusinessOnMap;
import static com.example.data_server.converters.CustomerEntityGrpcConverter.buildCustomerResponse;
import static com.example.data_server.converters.CustomerEntityGrpcConverter.generateCustomerFromRegisterCustomerRequest;

@GrpcService public class CustomerServiceImpl
    extends CustomerServiceGrpc.CustomerServiceImplBase
{

  private final CustomerRepository customerRepository;
  private final BusinessRepository businessRepository;

  @Autowired public CustomerServiceImpl(CustomerRepository customerRepository,
      BusinessRepository businessRepository)
  {
    this.customerRepository = customerRepository;
    this.businessRepository = businessRepository;
    System.out.println("CustomerServiceImpl created");

  }

  @Override public void registerCustomer(RegisterCustomerRequest request,
      StreamObserver<IdRequestResponse> responseObserver)
  {
    System.out.println("Request for register customer.");

    // Prepare to save the customer details in database
    Customer customer = generateCustomerFromRegisterCustomerRequest(
        request);

    try
    {
      Customer createdCustomer = customerRepository.save(customer);
      IdRequestResponse response = IdRequestResponse.newBuilder()
          .setId(createdCustomer.getId()).build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
    catch (org.springframework.dao.DuplicateKeyException e)
    {
      responseObserver.onError(new IllegalArgumentException(
          "Customer with email already exists: " + customer.getEmail()));
    }
  }

  @Override public void getCustomerByEmail(EmailRequestResponse request,
      StreamObserver<CustomerResponse> responseObserver)
  {
    Optional<Customer> customer = customerRepository.findByEmail(
        request.getEmail());
    if (customer.isPresent())
    {
      CustomerResponse response = buildCustomerResponse(customer.get());
      responseObserver.onNext(response);
      responseObserver.onCompleted();
      return;
    }

    throw new IllegalArgumentException("Customer not found");
  }

  @Override public void updateCustomerLocation(CustomerLocationRequest request,
      StreamObserver<BusinessesInRadiusResponse> responseObserver)
  {
    System.out.println("Request for updating customer location.");

    customerRepository.updateLocationById(request.getCustomerId(),
        request.getLatitude(), request.getLongitude(), request.getRadius());
    System.out.println(
        "Customer location updated: " + customerRepository.findById(
            request.getCustomerId()).get().getSearchRadius());

    double radiusRadians = request.getRadius() / GeoUtils.EARTH_RADIUS_KM;

    List<Business> businesses = businessRepository.findBusinessesWithinRadius(
        request.getLongitude(), request.getLatitude(), radiusRadians);

    BusinessesInRadiusResponse.Builder builder = BusinessesInRadiusResponse.newBuilder();

    for (Business business : businesses)
    {
      builder.addBusinesses(buildBusinessOnMap(business));
    }
    //Send the businesses in radius
    responseObserver.onNext(builder.build());
    responseObserver.onCompleted();
  }

  @Override public void getCustomerLocation(IdRequestResponse request,
      StreamObserver<CustomerLocationRequest> responseObserver)
  {
    Customer customer = customerRepository.findById(request.getId())
        .orElseThrow();
    CustomerLocationRequest response = CustomerLocationRequest.newBuilder()
        .setLatitude(customer.getLatitude())
        .setLongitude(customer.getLongitude())
        .setRadius(customer.getSearchRadius()).build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }


  @Override public void getCustomerById(IdRequestResponse request,
      StreamObserver<CustomerResponse> responseObserver) {
    Customer customer = customerRepository.findById(request.getId())
        .orElseThrow();
    CustomerResponse response = buildCustomerResponse(customer);
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void updateCustomerProfile(CustomerUpdateRequest request, StreamObserver<CustomerResponse> responseObserver) {
    try {
      System.out.println("Received update request for Customer ID: " + request.getId());

      // Validate Business ID
      if (request.getId().isEmpty()) {
        responseObserver.onError(
            Status.INVALID_ARGUMENT.withDescription("Customer ID is missing").asRuntimeException());
        return;
      }
      Customer customer = customerRepository.findById(request.getId()).get();

      Customer newCustomer = customer;
      newCustomer.setFirstName(request.getFirstName());
      newCustomer.setLastName(request.getLastName());
      newCustomer.setLastName(request.getLastName());
      newCustomer.setEmail(request.getEmail());
      newCustomer.setPhoneNumber(request.getPhoneNumber());


      customerRepository.delete(customer);
      customerRepository.save(customer);

      CustomerResponse response = CustomerEntityGrpcConverter.buildCustomerResponse(newCustomer);

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      System.err.println("Error updating customer profile: " + e.getMessage());

      // Prepare and send failure response
    }
  }
}

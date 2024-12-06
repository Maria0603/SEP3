package com.example.data_server.service;

import com.example.data_server.repository.BusinessRepository;
import com.example.data_server.repository.CustomerRepository;
import com.example.data_server.utility.GeoUtils;
import com.example.shared.converters.AddressConverter;
import com.example.sep3.grpc.*;
import com.example.shared.entities.usersEntities.Business;
import com.example.shared.entities.usersEntities.Customer;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.List;
import java.util.Optional;

import static converters.BusinessEntityGrpcConverter.*;

@GrpcService public class BusinessServiceImpl
    extends BusinessServiceGrpc.BusinessServiceImplBase
{
  private final BusinessRepository businessRepository;
  private final CustomerRepository customerRepository;

  @Autowired public BusinessServiceImpl(BusinessRepository businessRepository,
      CustomerRepository customerRepository)
  {
    this.businessRepository = businessRepository;
    System.out.println("BusinessServiceImpl created");
    this.customerRepository = customerRepository;
  }

  @Override public void registerBusiness(RegisterBusinessRequest request,
      StreamObserver<IdRequestResponse> responseObserver)
  {
    System.out.println("Request for register business.");

    // Prepare to save the business details in database
    Business business = generateBusinessFromRegisterBusinessRequest(
        request);

    Business createdBusiness = businessRepository.save(business);

    IdRequestResponse response = IdRequestResponse.newBuilder().setId(createdBusiness.getId()).build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override public void getBusinessByEmail(EmailRequestResponse request,
      StreamObserver<BusinessResponse> responseObserver)
  {
    Optional<Business> business = businessRepository.findByEmail(
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

  @Override public void getBusinessesInRadius(IdRequestResponse request,
      StreamObserver<BusinessesInRadiusResponse> responseObserver)
  {
    //extract the latitude, longitude and radius from customer
    System.out.println("Request for businesses by radius");
    Optional<Customer> customer = customerRepository.findById(
        request.getId());
    if (customer.isPresent())
    {
      double radiusRadians = customer.get().getSearchRadius() / GeoUtils.EARTH_RADIUS_KM;

      List<Business> businesses = businessRepository.findBusinessesWithinRadius(
          customer.get().getLongitude(), customer.get().getLatitude(),
          radiusRadians);
      System.out.println("Radius: " + customer.get().getSearchRadius());
      System.out.println("Lat: " + customer.get().getLatitude());
      System.out.println("Long: " + customer.get().getLongitude());

      BusinessesInRadiusResponse.Builder builder = BusinessesInRadiusResponse.newBuilder();
      for (Business business : businesses)
        builder.addBusinesses(buildBusinessOnMap(business));

      responseObserver.onNext(builder.build());
      responseObserver.onCompleted();
    }

  }


}

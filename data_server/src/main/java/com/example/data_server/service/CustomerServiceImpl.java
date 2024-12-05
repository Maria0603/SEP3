package com.example.data_server.service;

import com.example.data_server.repository.BusinessRepository;
import com.example.data_server.repository.CustomerRepository;
import com.example.sep3.grpc.*;
import com.example.shared.dao.usersDao.BusinessDao;
import com.example.shared.dao.usersDao.CustomerDao;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Point;

import java.util.List;

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

  @Override public void updateCustomerLocation(CustomerLocationRequest request,
      StreamObserver<BusinessesInRadiusResponse> responseObserver)
  {
    System.out.println("Request for updating customer location.");

    customerRepository.updateLocationByEmail(request.getCustomerEmail(),
        request.getLatitude(), request.getLongitude(), request.getRadius());

    double radiusRadians = request.getRadius() / 6378.1;

    List<BusinessDao> businesses = businessRepository.findBusinessesWithinRadius(
        request.getLongitude(), request.getLatitude(), radiusRadians);

    BusinessesInRadiusResponse.Builder builder = BusinessesInRadiusResponse.newBuilder();

    for (BusinessDao business : businesses)
    {
      builder.addBusinesses(buildBusinessOnMap(business));
    }
    //Send the businesses in radius
    responseObserver.onNext(builder.build());
    responseObserver.onCompleted();
  }

  @Override public void getCustomerLocation(CustomerByEmailRequest request,
      StreamObserver<CustomerLocationRequest> responseObserver)
  {
    CustomerDao customer = customerRepository.findByEmail(request.getEmail())
        .orElseThrow();
    CustomerLocationRequest response = CustomerLocationRequest.newBuilder()
        .setLatitude(customer.getLatitude())
        .setLongitude(customer.getLongitude())
        .setRadius(customer.getSearchRadius()).build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  private BusinessOnMap buildBusinessOnMap(BusinessDao dao)
  {
    return BusinessOnMap.newBuilder().setBusinessId(dao.getId())
        .setBusinessName(dao.getBusinessName()).setBusinessEmail(dao.getEmail())
        .setLogoPath(dao.getLogoPath()).setLatitude(dao.getLatitude())
        .setLongitude(dao.getLongitude()).build();
  }

}

package com.example.data_server.service;

import com.example.data_server.repository.BusinessRepository;
import com.example.data_server.repository.CustomerRepository;
import com.example.sep3.grpc.*;
import com.example.shared.dao.usersDao.BusinessDao;
import com.mongodb.client.model.geojson.Point;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

  @Override public void updateCustomerLocation(
      BusinessesInRadiusRequest request,
      StreamObserver<BusinessesInRadiusResponse> responseObserver)
  {
    System.out.println("Request for businesses by radius.");

    customerRepository.updateLocationByEmail(request.getCustomerEmail(), request.getLatitude(), request.getLongitude(), request.getRadius());

    //Send the businesses in radius
    responseObserver.onNext(getBusinessesInRadius(request));
    responseObserver.onCompleted();
  }

  public BusinessesInRadiusResponse getBusinessesInRadius(BusinessesInRadiusRequest request)
  {
    List<BusinessDao> businesses = businessRepository.findAll();

    BusinessesInRadiusResponse.Builder builder = BusinessesInRadiusResponse.newBuilder();
    for (BusinessDao business : businesses)
    {
      double distance = calculateDistance(request.getLatitude(),
          request.getLongitude(), business.getLatitude(),
          business.getLongitude());

      if (distance <= request.getRadius())
      {
        builder.addBusinesses(buildBusinessOnMap(business));
      }
    }
    return builder.build();
  }

  private BusinessOnMap buildBusinessOnMap(BusinessDao dao)
  {
    return BusinessOnMap.newBuilder().setBusinessId(dao.getId())
        .setBusinessName(dao.getBusinessName()).setBusinessEmail(dao.getEmail())
        .setLogoPath(dao.getLogoPath()).setLatitude(dao.getLatitude())
        .setLongitude(dao.getLongitude()).build();
  }

  private double calculateDistance(double lat1, double lon1, double lat2,
      double lon2)
  {
    final int R = 6371; // Earth radius in km
    double latDistance = Math.toRadians(lat2 - lat1);
    double lonDistance = Math.toRadians(lon2 - lon1);

    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
        + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
        * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c; // Distance in km
  }
}

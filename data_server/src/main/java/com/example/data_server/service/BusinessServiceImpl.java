package com.example.data_server.service;

import com.example.data_server.repository.BusinessRepository;
import com.example.data_server.repository.CustomerRepository;
import com.example.shared.converters.AddressConverter;
import com.example.sep3.grpc.*;
import com.example.shared.dao.usersDao.BusinessDao;
import com.example.shared.dao.usersDao.CustomerDao;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.List;
import java.util.Optional;

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

  @Override public void getBusinessesInRadius(BusinessesInRadiusRequest request,
      StreamObserver<BusinessesInRadiusResponse> responseObserver)
  {
    //extract the latitude, longitude and radius from customer
    Optional<CustomerDao> customer = customerRepository.findByEmail(
        request.getCustomerEmail());
    if (customer.isPresent())
    {
      double radiusRadians = customer.get().getSearchRadius() / 6378.1;

      List<BusinessDao> businesses = businessRepository.findBusinessesWithinRadius(
          customer.get().getLongitude(), customer.get().getLatitude(),
          radiusRadians);

      BusinessesInRadiusResponse.Builder builder = BusinessesInRadiusResponse.newBuilder();
      for (BusinessDao business : businesses)
        builder.addBusinesses(buildBusinessOnMap(business));

      responseObserver.onNext(builder.build());
      responseObserver.onCompleted();
    }

  }

  //just to test
  @Override public void getBusinessesInRadiuss(RadiusRequest request,
      StreamObserver<BusinessesInRadiusResponse> responseObserver)
  {
    System.out.println("Request for radiuss");
    double radius = 20 / 6378.1;

    List<BusinessDao> businesses = businessRepository.findBusinessesWithinRadius(
        request.getLng(), request.getLat(), radius);
    System.out.println(businesses.isEmpty());

    BusinessesInRadiusResponse.Builder builder = BusinessesInRadiusResponse.newBuilder();
    for (BusinessDao business : businesses)
      builder.addBusinesses(buildBusinessOnMap(business));

    responseObserver.onNext(builder.build());
    responseObserver.onCompleted();
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
    business.setLatitude(request.getLatitude());
    business.setLongitude(request.getLongitude());
    business.setLocation(
        new GeoJsonPoint(request.getLongitude(), request.getLatitude()));
    return business;
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

package com.example.data_server.converters;

import com.example.sep3.grpc.BusinessOnMap;
import com.example.sep3.grpc.BusinessResponse;
import com.example.sep3.grpc.RegisterBusinessRequest;
import com.example.shared.converters.AddressConverter;
import com.example.shared.entities.usersEntities.Business;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

public class BusinessEntityGrpcConverter
{

  public static BusinessResponse buildBusinessResponse(Business business)
  {
    return BusinessResponse.newBuilder()
        .setBusinessName(business.getBusinessName()).setAddress(
            AddressConverter.convertAddressToGrpcAddress(
                business.getAddress())).setEmail(business.getEmail())
        .setCvr(business.getCvr()).setHashedPassword(business.getPassword())
        .setId(business.getId()).setLogoPath(business.getLogoPath())
        .setPhoneNumber(business.getPhoneNumber()).setRole(business.getRole())
        .build();
  }

  public static Business generateBusinessFromRegisterBusinessRequest(
      RegisterBusinessRequest request)
  {
    Business business = new Business();
    business.setBusinessName(request.getBusinessName());
    business.setCvr(request.getCvr());
    business.setEmail(request.getEmail());
    business.setPassword(request.getHashedPassword());
    business.setPhoneNumber(request.getPhoneNumber());
    business.setLogoPath(request.getLogoPath());
    business.setRole(request.getRole());
    business.setAddress(
        AddressConverter.convertGrpcAddressToAddress(request.getAddress()));
    business.setLocation(
        new GeoJsonPoint(request.getLongitude(), request.getLatitude()));
    return business;
  }

  public static BusinessOnMap buildBusinessOnMap(Business business)
  {
    return BusinessOnMap.newBuilder().setBusinessId(business.getId())
        .setBusinessName(business.getBusinessName()).setBusinessEmail(business.getEmail())
        .setLogoPath(business.getLogoPath())
        .setLongitude(business.getLocation().getCoordinates().getFirst())
        .setLatitude(business.getLocation().getCoordinates().getLast()).build();
  }

}

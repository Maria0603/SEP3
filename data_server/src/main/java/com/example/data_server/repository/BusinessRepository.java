package com.example.data_server.repository;

import com.example.shared.entities.userEntities.Business;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BusinessRepository extends MongoRepository<Business, String>
{
  Optional<Business> findByEmail(String email);
  @Query("{ location: { $geoWithin: { $centerSphere: [[?0, ?1], ?2] } } }")
  List<Business> findBusinessesWithinRadius(double longitude, double latitude, double radius);

}



package com.example.data_server.repository;

import com.example.shared.dao.usersDao.CustomerDao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.util.Optional;

public interface CustomerRepository extends MongoRepository<CustomerDao, String>
{
  Optional<CustomerDao> findByEmail(String email);
  @Query("{ '_id': ?0 }")
  @Update("{ $set: { 'latitude': ?1, 'longitude': ?2,  'search_radius': ?3} }")
  void updateLocationById(String id, double latitude, double longitude, int radius);


}

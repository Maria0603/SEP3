package com.example.data_server.repository;

import com.example.shared.entities.domainEntities.Offer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OfferRepository extends MongoRepository<Offer, String>
{
  @Query("{ '_id': ?0 }")
  @Update("{ $set: { 'number_of_available_items': ?1} }")
  void updateNumberOfAvailableItems(String offerId, int numberOfItems);

  @Query("{ '_id': ?0 }")
  @Update("{ $set: { 'status': ?1} }")
  void updateStatus(String offerId, String status);

  @Query("{ 'business_id': ?0 }")
  List<Offer> findByBusinessId(String businessId);
}
 
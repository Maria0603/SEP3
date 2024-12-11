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
  List<Offer> findByStatus(String status);
  @Query("{ '_id': ?0 }")
  @Update("{ $set: { 'number_of_available_items': ?1} }")
  void updateNumberOfAvailableItems(String offerId, int numberOfItems);

  @Query("{ '_id': ?0 }")
  @Update("{ $set: { 'status': ?1} }")
  void updateStatus(String offerId, String status);

  @Query("{ 'categories' : { $in: ?0 } }")
  List<Offer> findByCategories(List<String> categories);

  @Query("{ 'pickup_time_start' : { $gte: ?0 }, 'pickup_time_end' : { $lte: ?1 } }")
  List<Offer> findByPickupTimeRange(LocalDateTime pickupTimeStart, LocalDateTime pickupTimeEnd);

  @Query("{ 'offer_price' : { $gte: ?0, $lte: ?1 } }")
  List<Offer> findByOfferPriceRange(int minPrice, int maxPrice);

  @Query("{ 'business_id': ?0 }")
  List<Offer> findByBusinessId(String businessId);
}
 
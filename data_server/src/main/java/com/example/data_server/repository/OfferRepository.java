package com.example.data_server.repository;

import com.example.shared.dao.domainDao.OfferDao;
import com.example.sep3.grpc.Time;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OfferRepository extends MongoRepository<OfferDao, String> {

  @Query("{ 'status' : ?0 }")
  List<OfferDao> findByStatus(String status);

  @Query("{ 'categories' : { $in: ?0 } }")
  List<OfferDao> findByCategories(List<String> categories);

  @Query("{ 'pickup_time_start' : { $gte: ?0 }, 'pickup_time_end' : { $lte: ?1 } }")
  List<OfferDao> findByPickupTimeRange(LocalDateTime pickupTimeStart, LocalDateTime pickupTimeEnd);

  @Query("{ 'offer_price' : { $gte: ?0, $lte: ?1 } }")
  List<OfferDao> findByOfferPriceRange(int minPrice, int maxPrice);
}
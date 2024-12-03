package com.example.data_server.repository;

import com.example.sep3.grpc.Time;
import com.example.shared.dao.OfferDao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends MongoRepository<OfferDao, String>
{
  List<OfferDao> findByStatus(String status);

  List<OfferDao> findByCategories(List<String> categories);

  List<OfferDao> findByPickupTimeRange(Time pickupTimeStart, Time pickupTimeEnd);

  List<OfferDao> findByOfferPriceRange(int minPrice, int maxPrice);
}


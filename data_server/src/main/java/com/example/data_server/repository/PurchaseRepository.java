package com.example.data_server.repository;

import com.example.shared.entities.domainEntities.Purchase;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends MongoRepository<Purchase, String> {

  @Query("{ 'customer_id' : ?0 }") List<Purchase> findByCustomerId(String customerId);
  @Query("{ 'business_id' : ?0 }") List<Purchase> findByBusinessId(String businessId);
  @Query("{ 'offer_id' : ?0 }") List<Purchase> findByOfferId(String offerId);
}
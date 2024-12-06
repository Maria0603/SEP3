package com.example.data_server.repository;

import com.example.shared.entities.domainEntities.Purchase;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends MongoRepository<Purchase, String> {
}
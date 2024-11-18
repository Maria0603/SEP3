package com.example.data_server.repository;

import com.example.shared.dao.PurchaseDao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends MongoRepository<PurchaseDao, String> {
}
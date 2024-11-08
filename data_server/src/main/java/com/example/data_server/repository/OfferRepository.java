package com.example.data_server.repository;

import com.example.shared.dao.OfferDao;
import com.example.shared.model.Status;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends MongoRepository<OfferDao, String>
{
  List<OfferDao> findByStatus(String status);
}
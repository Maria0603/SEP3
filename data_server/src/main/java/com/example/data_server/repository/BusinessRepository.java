package com.example.data_server.repository;

import com.example.shared.dao.OfferDao;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BusinessRepository extends MongoRepository<BusinessDao, String>
{

}

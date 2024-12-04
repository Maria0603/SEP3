package com.example.data_server.repository;

import com.example.shared.dao.domainDao.OrderDao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<OrderDao, String> {
}
package com.example.data_server.repository;

import com.example.shared.dao.usersDao.CustomerDao;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CustomerRepository extends MongoRepository<CustomerDao, String>
{
  Optional<CustomerDao> findByEmail(String email);
  void deleteByEmail(String email);
}

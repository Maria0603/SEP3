package com.example.data_server.repository;

import com.example.shared.dao.usersDao.BusinessDao;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BusinessRepository extends MongoRepository<BusinessDao, String>
{
  Optional<BusinessDao> findByEmail(String email);
  void deleteByEmail(String email);
}
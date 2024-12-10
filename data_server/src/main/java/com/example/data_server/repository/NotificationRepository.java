package com.example.data_server.repository;

import com.example.shared.entities.domainEntities.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String>
{
  List<Notification> findByUserIdAndUserRole(String userId, String userRole);
}


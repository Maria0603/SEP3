package com.example.data_server.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import static org.junit.jupiter.api.Assertions.*;

@TestConfiguration
class MongoConfigTest {

  @Bean
  public MongoTemplate mongoTemplate() {
    return new MongoTemplate(new SimpleMongoClientDatabaseFactory("mongodb://localhost:27017/test"));
  }
}
package com.example.data_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.io.IOException;

@SpringBootApplication @EnableMongoRepositories public class DataServerApplication
{

  public static void main(String[] args)
  {
    SpringApplication.run(DataServerApplication.class, args);
  }

}

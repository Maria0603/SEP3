package com.example.data_server;

import com.example.data_server.service.OfferServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication @EnableMongoRepositories public class DataServerApplication
{
  // @Autowired OfferServiceImpl offerService;

  public static void main(String[] args)
  {
    SpringApplication.run(DataServerApplication.class, args);
    System.out.println("Data Server running...");

  }

}

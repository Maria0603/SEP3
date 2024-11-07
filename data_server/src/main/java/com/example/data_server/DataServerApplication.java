package com.example.data_server;

import com.example.data_server.service.GrpcServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.io.IOException;

@SpringBootApplication @EnableMongoRepositories public class DataServerApplication
{

  @Autowired GrpcServer grpcServer;

  public static void main(String[] args)
  {
    SpringApplication.run(DataServerApplication.class, args);
  }

  @Override public void run(String... args) throws Exception
  {
    grpcServer.start();
  }

}

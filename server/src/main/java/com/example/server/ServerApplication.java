package com.example.server;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@SpringBootApplication @EnableMongoRepositories(basePackages = "com.example.server") @EnableCaching public class ServerApplication
{
  public static void main(String[] args)
  {
    ApplicationContext context = SpringApplication.run(ServerApplication.class,
        args);

    System.out.println("Server running...");

  }

  @Bean public DataServerStub dataServerStub()
  {
    return new DataServerStub("localhost", 8081);
  }




}

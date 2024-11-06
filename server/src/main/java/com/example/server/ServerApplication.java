package com.example.server;

import com.example.server.Services.CollectService.CollectServiceImpl;
import com.example.server.Services.FoodManagementService.FoodManagementServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.io.IOException;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.example.sep_poc")
@EnableCaching
public class ServerApplication
{
  @Autowired
  private FoodManagementServiceImpl foodManagementServiceImpl;
  @Autowired
  private CollectServiceImpl collectServiceImpl;

  public static void main(String[] args)
  {
    SpringApplication.run(ServerApplication.class, args);
    System.out.println("Server running...");
  }

  @Bean
  public Server grpcFoodManagementService() throws IOException
  {
    Server server = ServerBuilder.forPort(50051)
        .addService(foodManagementServiceImpl)
        .build()
        .start();
    System.out.println("gRPC food management server started on port: 50051");
    handleShutdownServer(server);
    return server;
  }

  @Bean
  public Server grpcCollectService() throws IOException
  {
    Server server = ServerBuilder.forPort(50052)
        .addService(collectServiceImpl)
        .build()
        .start();
    System.out.println("gRPC collect server started on port: 50052");
    handleShutdownServer(server);
    return server;
  }

  private void handleShutdownServer(Server server)
  {
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      System.out.println("Shutting down gRPC server...");
      server.shutdown();
      System.out.println("gRPC server shut down.");
    }));
  }



}

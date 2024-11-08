package com.example.data_server;

import com.example.data_server.service.GrpcServer;
import com.example.data_server.service.OfferServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.BindableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.io.IOException;

@SpringBootApplication @EnableMongoRepositories public class DataServerApplication implements CommandLineRunner
{

  @Autowired GrpcServer grpcServer;
  @Autowired private OfferServiceImpl offerServiceImpl;

  public static void main(String[] args)
  {
    SpringApplication.run(DataServerApplication.class, args);

  }

  @Override public void run(String... args) throws Exception
  {
    grpcServer.start();
  }
  /*
  @Bean
  public Server grpcOfferService() throws IOException
  {
    Server server = ServerBuilder.forPort(8081)
        .addService((BindableService) offerServiceImpl)
        .build()
        .start();
    System.out.println("gRPC offer server started on port: 50051");
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
  }*/
}

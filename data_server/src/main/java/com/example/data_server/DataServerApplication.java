package com.example.data_server;

import com.example.data_server.service.OfferGrpcServer;
import com.example.data_server.service.OfferServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication @EnableMongoRepositories public class DataServerApplication implements CommandLineRunner
{

  @Autowired OfferGrpcServer offerGrpcServer;

  public static void main(String[] args)
  {
    SpringApplication.run(DataServerApplication.class, args);

  }

  @Override public void run(String... args) throws Exception
  {
    offerGrpcServer.start();
  }
/*
  @Bean
  public Server grpcOfferService() throws IOException
  {
    Server server = ServerBuilder.forPort(8081)
        .addService(offerServiceImpl)
        .build()
        .start();
    System.out.println("gRPC offer server started on port: 8081");
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

package com.example.server;

import com.example.sep3.grpc.*;
import com.example.server.Services.CollectService.CollectServiceImpl;
import com.example.server.Services.FoodManagementService.FoodManagementServiceImpl;
import com.google.protobuf.ByteString;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Array;
import java.sql.SQLOutput;
import java.util.ArrayList;

@SpringBootApplication @EnableMongoRepositories(basePackages = "com.example.server") @EnableCaching public class ServerApplication
{
  @Autowired private FoodManagementServiceImpl foodManagementServiceImpl;
  @Autowired private CollectServiceImpl collectServiceImpl;

  public static void main(String[] args)
  {
    SpringApplication.run(ServerApplication.class, args);
    System.out.println("Server running...");

    GrpcClient grpcClient = new GrpcClient("localhost", 9090);

    OfferList offerList = grpcClient.getAllAvailableOffers();
    System.out.println("Offers:  " + offerList);

    ArrayList<String> categories = new ArrayList<>();
    categories.add("Food");
    categories.add("Vegan");
    Date pickupDate = Date.newBuilder().setDay(3).setMonth(6).setYear(2020)
        .build();
    Time pickupTimeStart = Time.newBuilder().setHour(10).setMinute(30).build();
    Time pickupTimeEnd = Time.newBuilder().setHour(12).setMinute(30).build();
    byte[] imageBytes = loadImageAsByteArray(
        "../../../../../../../data_server/src/main/images/image.jpg");

    SaveOfferRequest request = SaveOfferRequest.newBuilder()
        .setTitle("Titleeeee").setDescription("Description").setPrice(20)
        .setNumberOfFoodBags(3).addAllCategories(categories)
        .setPickupDate(pickupDate).setPickupTimeStart(pickupTimeStart)
        .setPickupTimeEnd(pickupTimeEnd)
        .setImage(ByteString.copyFrom(imageBytes)).build();

    SaveOfferResponse saveResponse = grpcClient.saveOffer(request);

    System.out.println("Saved offer id:   " + saveResponse.getId());

    grpcClient.shutdown();
  }

  private static byte[] loadImageAsByteArray(String imagePath)
  {
    File file = new File(imagePath);
    try
    {
      return Files.readAllBytes(file.toPath());
    }
    catch (IOException e)
    {
      e.printStackTrace();
      return new byte[0]; // Return an empty byte array if there's an error
    }
  }

/*
  @Bean
  public Server grpcFoodManagementService() throws IOException
  {
    Server server = ServerBuilder.forPort(50051)
        .addService((BindableService) foodManagementServiceImpl)
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
        .addService((BindableService) collectServiceImpl)
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
  }*/

}

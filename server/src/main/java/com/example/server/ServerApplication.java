package com.example.server;

import com.example.sep3.grpc.*;
//import com.example.server.Services.CollectService.CollectServiceImpl;
//import com.example.server.Services.FoodManagementService.FoodManagementServiceImpl;
import com.google.protobuf.ByteString;
import io.grpc.BindableService;
import io.grpc.Grpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.sql.Array;
import java.sql.SQLOutput;
import java.util.ArrayList;

@SpringBootApplication @EnableMongoRepositories(basePackages = "com.example.server") @EnableCaching public class ServerApplication
{
  public static void main(String[] args)
  {
    ApplicationContext context = SpringApplication.run(ServerApplication.class, args);
    System.out.println("Server running...");

    //GrpcClient grpcClient = new GrpcClient("localhost", 9091);

    GrpcClient grpcClient = context.getBean(GrpcClient.class);
    OfferList offerList = grpcClient.getAllAvailableOffers(EmptyMessage.newBuilder().build());
    System.out.println("Offers:  " + offerList);

    ArrayList<String> categories = new ArrayList<>();
    categories.add("Food");
    categories.add("Vegan");
    Date pickupDate = Date.newBuilder().setDay(3).setMonth(6).setYear(2020)
        .build();
    Time pickupTimeStart = Time.newBuilder().setHour(10).setMinute(30).build();
    Time pickupTimeEnd = Time.newBuilder().setHour(12).setMinute(30).build();
    byte[] imageBytes=null;
    try
    {
      imageBytes = createImageByteArray();
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }

    SaveOfferRequest request = SaveOfferRequest.newBuilder()
        .setTitle("Titleeeee").setDescription("Description").setOfferPrice(20).setOriginalPrice(300)
        .setNumberOfItems(3).addAllCategories(categories)
        .setPickupDate(pickupDate).setPickupTimeStart(pickupTimeStart)
        .setPickupTimeEnd(pickupTimeEnd)
        .setImage(ByteString.copyFrom(imageBytes)).build();

    SaveOfferResponse saveResponse = grpcClient.saveOffer(request);

    System.out.println("Saved offer id:   " + saveResponse.getId());

    grpcClient.shutdown();
  }

  @Bean public GrpcClient grpcClient()
  {
    return new GrpcClient("localhost", 8081);
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
  //Method to create a dummy red image, for testing purposes; do not delete
  private static byte[] createImageByteArray() throws IOException
  {
    // Create a 200x200 BufferedImage with RGB color
    BufferedImage bufferedImage = new BufferedImage(200, 200,
        BufferedImage.TYPE_INT_RGB);

    // Fill the image with a solid color
    for (int y = 0; y < 200; y++)
      for (int x = 0; x < 200; x++)
        bufferedImage.setRGB(x, y, (255 << 16) | (0 << 8) | 0); // Red

    // Convert BufferedImage to byte array in JPEG format
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(bufferedImage, "jpg", baos);
    return baos.toByteArray();
  }


}

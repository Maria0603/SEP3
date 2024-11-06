package com.example.server.Services.FoodManagementService;

import com.example.server.Model.FoodBag;
import com.example.server.Database.MongoDbRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import proto.FoodBag.*;
import proto.FoodManagementServiceGrpc;

@Service public class FoodManagementServiceImpl
    extends FoodManagementServiceGrpc.FoodManagementServiceImplBase
{
  private MongoDbRepository foodBagRepository;

  @Autowired private RedisTemplate<String, FoodBag> redisTemplate;

  public FoodManagementServiceImpl(MongoDbRepository foodBagRepository)
  {
    this.foodBagRepository = foodBagRepository;
    System.out.println("FoodManagementServiceImpl created");
  }

  @Override public void addFoodBag(FoodBagRequest request,
      StreamObserver<FoodBagResponse> responseObserver)
  {
    System.out.println("Received Request ??? => " + request.toString());

    FoodBag foodBag = new FoodBag();

    foodBag.setAddress(request.getAddress());
    foodBag.setPickupTime1(request.getPickupTime1());
    foodBag.setPickupTime2(request.getPickupTime2());
    foodBag.setType(request.getType());
    foodBag.setPickupDate(request.getPickUpDate());
    foodBag.setCollector("No collector");
    foodBag.setPrice((short) request.getPrice());

    foodBagRepository.save(foodBag);

    //Caching with Redis
    //redisTemplate.opsForList().rightPush("foodBagsCache", foodBag);

    FoodBagResponse response = FoodBagResponse.newBuilder()
        .setId(foodBag.getId()).setAddress(foodBag.getAddress())
        .setPickupTime1(foodBag.getPickupTime1())
        .setPickupTime2(foodBag.getPickupTime2()).setType(foodBag.getType())
        .setPickUpDate(foodBag.getPickupDate()).setCollector("No collector")
        .setPrice(foodBag.getPrice()).build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();

  }
}


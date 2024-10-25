package com.example.sep_poc.Services.CollectService;

import com.example.sep_poc.Model.FoodBag;
import com.example.sep_poc.Database.MongoDbRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import proto.FoodBag.*;
import proto.CollectServiceGrpc.CollectServiceImplBase;

import java.util.List;

@Service public class CollectServiceImpl extends CollectServiceImplBase
{

  private final MongoDbRepository foodBagRepository;

  @Autowired private RedisTemplate<String, FoodBag> redisTemplate;

  public CollectServiceImpl(MongoDbRepository foodBagRepository)
  {
    this.foodBagRepository = foodBagRepository;
    System.out.println("CollectServiceImpl created");
  }

  @Override public void getFoodBags(EmptyMessage request,
      StreamObserver<FoodBagList> responseObserver)
  {
    displayRequest(request.toString());

    List<FoodBag> foodBags = foodBagRepository.findAll();

    //Caching with Redis
    /*
    List<FoodBag> foodBags = redisTemplate.opsForList().range("foodBagsCache", 0, -1);

    if (foodBags == null || foodBags.isEmpty()) {
      foodBags = foodBagRepository.findAll();

      // Cache the entire list of food bags
      for (FoodBag foodBag : foodBags) {
        redisTemplate.opsForList().rightPush("foodBagsCache", foodBag);
      }
    }*/

    buildAndSendFoodBagList(responseObserver, foodBags);
  }

  @Override public void getFoodBagsBetween(PriceIntervalRequest request,
      StreamObserver<FoodBagList> responseObserver)
  {
    displayRequest(request.toString());

    List<FoodBag> foodBags = foodBagRepository.findFoodBagsWithinPriceRange(
        (short) request.getPrice1(), (short) request.getPrice2());

    buildAndSendFoodBagList(responseObserver, foodBags);
  }

  private void buildAndSendFoodBagList(
      StreamObserver<FoodBagList> responseObserver, List<FoodBag> foodBags)
  {
    FoodBagList.Builder foodBagListBuilder = FoodBagList.newBuilder();

    for (FoodBag foodBag : foodBags)
    {
      FoodBagResponse grpcFoodBag = FoodBagResponse.newBuilder()
          .setId(foodBag.getId()).setAddress(foodBag.getAddress())
          .setPickupTime1(foodBag.getPickupTime1())
          .setPickupTime2(foodBag.getPickupTime2()).setType(foodBag.getType())
          .setCollector(foodBag.getCollector())
          .setPickUpDate(foodBag.getPickupDate()).setPrice(foodBag.getPrice())
          .build();

      foodBagListBuilder.addFoodBags(grpcFoodBag);
    }

    FoodBagList foodBagList = foodBagListBuilder.build();
    responseObserver.onNext(foodBagList);
    responseObserver.onCompleted();
  }

  //Just for testing
  private void displayRequest(String request)
  {
    System.out.println("Received Request => " + request);
  }
}

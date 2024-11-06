package com.example.server.Services.CollectService;

import com.example.server.Model.FoodBag;
import com.example.server.Database.MongoDbRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service public class CollectService
{
  private MongoDbRepository repository;

  public CollectService(MongoDbRepository repository)
  {
    this.repository = repository;
    System.out.println("CollectService created");

  }

  public List<FoodBag> getAvailableBags()
  {
    return repository.findAll();
  }

  public List<FoodBag> getFoodBagsInPriceRange(short price1, short price2)
  {
    return repository.findFoodBagsWithinPriceRange(price1, price2);
  }

  @Cacheable("extra_food_bags") public List<FoodBag> getCachedFoodBags()
  {
    return repository.findAll();
  }

}


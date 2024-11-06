package com.example.server.Services.FoodManagementService;

import com.example.server.Model.FoodBag;
import com.example.server.Database.MongoDbRepository;
import org.springframework.stereotype.Service;

@Service public class FoodManagementService
{
  private MongoDbRepository repository;

  public FoodManagementService(MongoDbRepository repository)
  {
    this.repository = repository;
    System.out.println("FoodManagementService created");
  }

  public FoodBag saveFoodBag(FoodBag foodBag)
  {
    return repository.save(foodBag);
  }

}


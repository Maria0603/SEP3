package com.example.sep_poc.Services.FoodManagementService;

import com.example.sep_poc.Model.FoodBag;
import com.example.sep_poc.Database.MongoDbRepository;
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


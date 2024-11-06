package com.example.server.Database;

import com.example.server.Model.FoodBag;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository public interface MongoDbRepository
    extends MongoRepository<FoodBag, String>
{
  @Query("{ 'price' : { $gte: ?0, $lte: ?1 } }")
  List<FoodBag> findFoodBagsWithinPriceRange(short price1, short price2);
}



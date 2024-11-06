package com.example.server.Contollers;

import com.example.server.Model.FoodBag;
import com.example.server.Services.CollectService.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/foodbags") public class CollectController
{
  @Autowired private CollectService collectService;

  @GetMapping public ResponseEntity<List<FoodBag>> getAvailableFoodBags()
  {
    return ResponseEntity.ok(collectService.getAvailableBags());
  }

  @GetMapping("/all") public List<FoodBag> getAllFoodBags()
  {
    return collectService.getAvailableBags();
  }

  @GetMapping("/price-range") public List<FoodBag> getFoodBagsInRange(
      @RequestParam short price1, @RequestParam short price2)
  {
    return collectService.getFoodBagsInPriceRange(price1, price2);
  }

}


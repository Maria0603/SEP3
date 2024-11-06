package com.example.server.Contollers;

import com.example.server.Model.FoodBag;
import com.example.server.Services.FoodManagementService.FoodManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/foodbags/add") public class FoodManagementController
{
  @Autowired private FoodManagementService foodManagementService;

  @PostMapping public FoodBag createFoodBag(@RequestBody FoodBag foodBag)
  {
    return foodManagementService.saveFoodBag(foodBag);
  }

}


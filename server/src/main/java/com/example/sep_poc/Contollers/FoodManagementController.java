package com.example.sep_poc.Contollers;

import com.example.sep_poc.Model.FoodBag;
import com.example.sep_poc.Services.FoodManagementService.FoodManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/foodbags/add") public class FoodManagementController
{
  @Autowired private FoodManagementService foodManagementService;

  @PostMapping public FoodBag createFoodBag(@RequestBody FoodBag foodBag)
  {
    return foodManagementService.saveFoodBag(foodBag);
  }

}


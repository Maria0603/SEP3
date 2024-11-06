package com.example.server.Contollers;

import com.example.server.Services.MongoDbService;
import com.example.server.dao.OfferDao;
import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/main/offers") public class MainServerController
{
  private final MongoDbService mongoDbService;

  @Autowired public MainServerController(MongoDbService mongoDbService)
  {
    this.mongoDbService = mongoDbService;
  }

  @GetMapping public List<OfferDao> getAllOffers()
  {
    return mongoDbService.getAllOffers();
  }

  @GetMapping("/{id}") public OfferDao getOfferById(@PathVariable String id)
  {
    return mongoDbService.getOfferById(id);
  }

  @PostMapping public OfferDao createOffer(@RequestBody OfferDao offer)
  {
    return mongoDbService.createOffer(offer);
  }
}

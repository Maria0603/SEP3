package com.example.server.Services;

import com.example.server.dao.FoodBagDao;
import com.example.server.dao.OfferDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service public class MongoDbService
{
  private final RestTemplate restTemplate;

  @Value("${data.server.url}")
  private String dataServerUrl;

  public MongoDbService(RestTemplateBuilder builder)
  {
    this.restTemplate = builder.build();
  }
  public List<OfferDao> getAllOffers() {
    String url = dataServerUrl + "/offer";
    ResponseEntity<OfferDao[]> response = restTemplate.getForEntity(url, OfferDao[].class);
    return List.of(response.getBody());
  }

  public FoodBagDao getFoodBagById(String id)
  {
    String url = dataServerUrl+"/food_bag/" + id;
    ResponseEntity<FoodBagDao> response = restTemplate.getForEntity(url, FoodBagDao.class);
    return response.getBody();
  }
  public OfferDao getOfferById(String id)
  {
    String url = dataServerUrl+"/offer/" + id;
    ResponseEntity<OfferDao> response = restTemplate.getForEntity(url, OfferDao.class);
    return response.getBody();
  }

  public OfferDao createOffer(OfferDao offer)
  {
    String url = dataServerUrl+"/offers";
    ResponseEntity<OfferDao> response = restTemplate.postForEntity(url, offer,
        OfferDao.class);
    return response.getBody();
  }

}

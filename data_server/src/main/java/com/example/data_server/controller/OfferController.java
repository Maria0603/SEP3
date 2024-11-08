package com.example.data_server.controller;

import com.example.shared.dao.OfferDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.data_server.service.OfferServiceImpl;

import java.util.List;
/*
@RestController @RequestMapping("/api/offer") public class OfferController
{
  private final OfferServiceImpl offerServiceImpl;

  @Autowired public OfferController(OfferServiceImpl offerServiceImpl)
  {
    this.offerServiceImpl = offerServiceImpl;
  }

  @GetMapping public List<OfferDao> getAllOffers()
  {
    return offerServiceImpl.getAllOffers();
  }

  @GetMapping("/{id}") public ResponseEntity<OfferDao> getOfferById(
      @PathVariable String id)
  {
    return offerServiceImpl.getOfferById(id).map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping public OfferDao createOffer(@RequestBody OfferDao offer)
  {
    return null;//offerServiceImpl.saveOffer(offer);
  }



}*/

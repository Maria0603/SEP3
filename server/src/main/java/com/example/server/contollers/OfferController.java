package com.example.server.contollers;

import com.example.sep3.grpc.OfferIdRequest;
import com.example.sep3.grpc.SaveOfferResponse;
import com.example.server.dto.CreateOfferRequestDto;
import com.example.server.dto.OrderRequestDto;
import com.example.server.services.OfferService;
import com.example.shared.dao.OfferDao;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController @RequestMapping("/offers") public class OfferController
{

  private final OfferService offerService;

  public OfferController(OfferService offerService)
  {
    this.offerService = offerService;
  }

  @PostMapping public ResponseEntity<String> saveOffer(
      @Valid @RequestBody CreateOfferRequestDto offerRequestDto)
  {
    try
    {
      //Maybe send the whole object to the client?
      String offerId = offerService.saveOffer(offerRequestDto);

      return ResponseEntity.ok("Offer created with ID: " + offerId);
    }
    catch (Exception e)
    {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/{id}") public ResponseEntity<Map<String, String>> placeOrder(
      @PathVariable String id, @RequestBody OrderRequestDto orderRequest)
  {
    return ResponseEntity.ok(new HashMap<>());
  }

}
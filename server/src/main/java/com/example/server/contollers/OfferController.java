package com.example.server.contollers;

import com.example.server.dto.CreateOfferRequestDto;
import com.example.server.dto.PlaceOrderRequestDto;
import com.example.server.dto.PlaceOrderResponseDto;
import com.example.server.services.OfferService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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

  //@PostMapping("/{id}")
  @PostMapping("/order") //TODO: change
  public ResponseEntity<PlaceOrderResponseDto> placeOrder(//@PathVariable String id,
       @RequestBody PlaceOrderRequestDto orderRequest)
  {
    System.out.println("Request for place order.");
    try
    {
      PlaceOrderResponseDto response = offerService.placeOrder(orderRequest);
      return ResponseEntity.ok(response);
    }
    catch (IllegalArgumentException e)
    {
      e.printStackTrace();
      return ResponseEntity.badRequest().build(); //TODO: update
    }
  }
}

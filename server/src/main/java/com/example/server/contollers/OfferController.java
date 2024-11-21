package com.example.server.contollers;

import com.example.server.dto.*;
import com.example.server.services.OfferService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
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

  //Look at OfferTestClient to see how the request should look like
  @PostMapping(consumes = "multipart/form-data") public ResponseEntity<OfferResponseDto> saveOffer(
      @Valid @RequestPart("offer") CreateOfferRequestDto offerRequestDto,
      @RequestPart("file") MultipartFile file)
  {
    try
    {
      offerRequestDto.setImage(file.getBytes()); //sent separately
      OfferResponseDto offerDto = offerService.saveOffer(offerRequestDto);

      return ResponseEntity.ok(offerDto);
    }
    catch (Exception e)
    {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  @GetMapping public ResponseEntity<List<ShortOfferResponseDto>> getAvailableOffers()
  {
    try
    {
      List<ShortOfferResponseDto> offers = offerService.getAvailableOffers();

      return ResponseEntity.ok(offers);
    }
    catch (Exception e)
    {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  //@PostMapping("/{id}")
  @PostMapping("/order") //TODO: change
  public ResponseEntity<PlaceOrderResponseDto> placeOrder(
      //@PathVariable String id,
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

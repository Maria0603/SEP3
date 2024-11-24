package com.example.server.contollers;

import com.example.sep3.grpc.*;
import com.example.server.dto.offer.ShortOfferResponseDto;
import com.example.server.dto.order.PlaceOrderRequestDto;
import com.example.server.dto.order.PlaceOrderResponseDto;
import com.example.server.services.OfferService;
import com.example.server.dto.offer.CreateOfferRequestDto;
import com.example.server.dto.date_time.DateDto;
import com.example.server.dto.offer.OfferResponseDto;
import com.example.server.dto.date_time.TimeDto;
import io.grpc.stub.StreamObserver;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

@RestController @RequestMapping("/offers") public class OfferController
{

  private final OfferService offerService;

  public OfferController(OfferService offerService)
  {
    this.offerService = offerService;

  }

  //Look at OfferTestClient to see how the request should look like
  @PostMapping public ResponseEntity<OfferResponseDto> saveOffer(
      @Valid @RequestBody CreateOfferRequestDto offerRequestDto)
  {
    try
    {
      OfferResponseDto offerDto = offerService.saveOffer(offerRequestDto);
      return ResponseEntity.ok(offerDto);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  @GetMapping public ResponseEntity<List<ShortOfferResponseDto>> getShortAvailableOffers()
  {
    try
    {
      List<ShortOfferResponseDto> offers = offerService.getAvailableOffers();

      return ResponseEntity.ok(offers);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }
  }


}

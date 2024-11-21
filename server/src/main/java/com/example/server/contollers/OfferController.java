package com.example.server.contollers;

import com.example.sep3.grpc.*;
import com.example.server.dto.*;
import com.example.server.services.OfferService;
import com.example.server.dto.CreateOfferRequestDto;
import com.example.server.dto.DateDto;
import com.example.server.dto.OfferResponseDto;
import com.example.server.dto.TimeDto;
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
  // TODO: business information, like on blazorclient
  private OfferResponseDto mapToOfferResponseDto(ShortOfferResponse offer) {
    OfferResponseDto offerResponseDto = new OfferResponseDto();
    offerResponseDto.setId(offer.getId());
    offerResponseDto.setTitle(offer.getTitle());
    offerResponseDto.setOriginalPrice(offer.getOriginalPrice());
    offerResponseDto.setOfferPrice(offer.getOriginalPrice());
    offerResponseDto.setNumberOfItems(offer.getNumberOfItems());
    DateDto date = new DateDto();
    date.setDay(offer.getPickupDate().getDay());
    date.setMonth(offer.getPickupDate().getMonth());
    date.setYear(offer.getPickupDate().getYear());
    offerResponseDto.setPickupDate(date);
    TimeDto timeStart = new TimeDto();
    timeStart.setHour(offer.getPickupTimeStart().getHour());
    timeStart.setMinute(offer.getPickupTimeStart().getMinute());
    offerResponseDto.setPickupTimeStart(timeStart);
    TimeDto timeEnd = new TimeDto();
    timeStart.setHour(offer.getPickupTimeEnd().getHour());
    timeStart.setMinute(offer.getPickupTimeEnd().getMinute());
    offerResponseDto.setPickupTimeEnd(timeEnd);
//    offerResponseDto.setCategories(offer.getCategories(0));
//    offerResponseDto.setImage(offer.getImage());
    offerResponseDto.setStatus(offer.getStatus());

    return offerResponseDto;
  }


}

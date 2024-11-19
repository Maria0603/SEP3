package com.example.server.contollers;

import com.example.sep3.grpc.EmptyMessage;
import com.example.sep3.grpc.OfferList;
import com.example.sep3.grpc.OfferResponse;
import com.example.sep3.grpc.SaveOfferResponse;
import com.example.server.dto.CreateOfferRequestDto;
import com.example.server.dto.OfferResponseDto;
import com.example.server.services.OfferService;
import io.grpc.stub.StreamObserver;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

@RestController @RequestMapping("/api/offers") public class OfferController
{

  private final OfferService offerService;

  public OfferController(OfferService offerService)
  {
    this.offerService = offerService;

  }

  @GetMapping("/test")
  public ResponseEntity<String> testEndpoint() {
    return ResponseEntity.ok("Test passed");
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
  @GetMapping
  public ResponseEntity<List<OfferResponseDto>> getOffers() {

    EmptyMessage request = EmptyMessage.newBuilder().build();
    List<OfferResponseDto> responseDtos = new ArrayList<>();

    CountDownLatch latch = new CountDownLatch(1);

    offerService.getAvailableOffers(request, new StreamObserver<OfferList>() {
      @Override
      public void onNext(OfferList offerList) {
        // Collect all offers from the gRPC response
        responseDtos.addAll(
                offerList.getOfferList().stream()
                        .map(offer -> mapToOfferResponseDto(offer)) // Optional: Transform if necessary
                        .toList()
        );
      }

      @Override
      public void onError(Throwable throwable) {
        latch.countDown();
        throw new RuntimeException("Error retrieving offers: " + throwable.getMessage(), throwable);
      }

      @Override
      public void onCompleted() {

        latch.countDown();
      }
    });



    try {
      latch.await(); // Wait for the gRPC call to complete
    } catch (InterruptedException e) {
      throw new RuntimeException("Interrupted while waiting for gRPC response", e);
    }
    //  JUST A PRINTOUT
    for (OfferResponseDto responseDto : responseDtos){
      System.out.println(responseDto.getTitle());
    }

    return ResponseEntity.ok(responseDtos);
  }

  // TODO: business information, like on blazorclient
  private OfferResponseDto mapToOfferResponseDto(OfferResponse offer) {
    OfferResponseDto offerResponseDto = new OfferResponseDto();
    offerResponseDto.setId(offer.getId());
    offerResponseDto.setTitle(offer.getTitle());
    offerResponseDto.setDescription(offer.getDescription());
    offerResponseDto.setOriginalPrice(offer.getOriginalPrice());
    offerResponseDto.setOfferPrice(offer.getOriginalPrice());
    offerResponseDto.setNumberOfItems(offer.getNumberOfItems());
//    offerResponseDto.setPickupDate(offer.getPickupDate());
//    offerResponseDto.setPickupTimeStart(offer.getPickupTimeStart());
//    offerResponseDto.setPickupTimeEnd(offer.getPickupTimeEnd());
//    offerResponseDto.setCategories(offer.getCategories(0));
//    offerResponseDto.setImage(offer.getImage());
    offerResponseDto.setStatus(offer.getStatus());

    return offerResponseDto;
  }


}
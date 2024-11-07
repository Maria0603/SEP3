package com.example.server.Contollers;

import com.example.sep3.grpc.SaveOfferRequest;
import com.example.sep3.grpc.SaveOfferResponse;
import com.example.server.GrpcClient;
import com.example.server.dto.CreateOfferRequestDto;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/offers") public class OfferController
{
  private final GrpcClient grpcClient;

  @Autowired public OfferController(GrpcClient grpcClient)
  {
    this.grpcClient = grpcClient;
  }

  @PostMapping("/create") public ResponseEntity<String> createOffer(
      @RequestBody CreateOfferRequestDto offerRequestDto)
  {
    SaveOfferRequest request = SaveOfferRequest.newBuilder()
        .setTitle(offerRequestDto.getTitle())
        .setDescription(offerRequestDto.getDescription())
        .setPrice(offerRequestDto.getPrice())
        .setNumberOfFoodBags(offerRequestDto.getNumberOfFoodBags())
        .addAllCategories(offerRequestDto.getCategories())
        .setImage(ByteString.copyFrom(offerRequestDto.getImage()))
        .setPickupDate(offerRequestDto.getPickupDate())
        .setPickupTimeStart(offerRequestDto.getPickupTimeStart())
        .setPickupTimeEnd(offerRequestDto.getPickupTimeEnd()).build();

    SaveOfferResponse response=grpcClient.saveOffer(request);
    return ResponseEntity.ok("Offer created with ID: " + response.getId());
  }
}

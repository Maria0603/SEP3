package com.example.server.contollers;

import com.example.sep3.grpc.Date;
import com.example.sep3.grpc.SaveOfferRequest;
import com.example.sep3.grpc.SaveOfferResponse;
import com.example.sep3.grpc.Time;
import com.example.server.GrpcClient;
import com.example.server.dto.CreateOfferRequestDto;
import com.example.server.utility.DateConverter;
import com.example.server.utility.TimeConverter;
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

  @PostMapping("/new") public ResponseEntity<String> saveOffer(
      @RequestBody CreateOfferRequestDto offerRequestDto)
  {
    try
    {
      SaveOfferResponse response = grpcClient.saveOffer(toSaveOfferRequest(offerRequestDto));
      return ResponseEntity.ok("Offer created with ID: " + response.getId());
    }
    catch(Exception e)
    {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }


  private SaveOfferRequest toSaveOfferRequest(CreateOfferRequestDto createOfferRequestDto) {
    // Create SaveOfferRequest builder
    SaveOfferRequest.Builder saveOfferRequestBuilder = SaveOfferRequest.newBuilder();

    // Mapping simple fields
    saveOfferRequestBuilder.setTitle(createOfferRequestDto.getTitle());
    saveOfferRequestBuilder.setDescription(createOfferRequestDto.getDescription());
    saveOfferRequestBuilder.setOriginalPrice(createOfferRequestDto.getOriginalPrice());
    saveOfferRequestBuilder.setOfferPrice(createOfferRequestDto.getOfferPrice());
    saveOfferRequestBuilder.setNumberOfItems(createOfferRequestDto.getNumberOfItems());
    saveOfferRequestBuilder.addAllCategories(createOfferRequestDto.getCategories());
    saveOfferRequestBuilder.setImage(com.google.protobuf.ByteString.copyFrom(createOfferRequestDto.getImage()));

    // Convert DateDto to Date
    Date pickupDate = DateConverter.convertDateDtoToGrpcDate(createOfferRequestDto.getPickupDate());
    saveOfferRequestBuilder.setPickupDate(pickupDate);

    // Convert TimeDto to Time for start and end time
    Time pickupTimeStart = TimeConverter.convertTimeDtoToGrpcTime(createOfferRequestDto.getPickupTimeStart());
    saveOfferRequestBuilder.setPickupTimeStart(pickupTimeStart);

    Time pickupTimeEnd = TimeConverter.convertTimeDtoToGrpcTime(createOfferRequestDto.getPickupTimeEnd());
    saveOfferRequestBuilder.setPickupTimeEnd(pickupTimeEnd);

    // Build and return the SaveOfferRequest object
    return saveOfferRequestBuilder.build();
  }
}
package com.example.server.services;

import com.example.sep3.grpc.*;
import com.example.server.DataServerStub;
import com.example.server.converters.FullOfferListToShortOfferListConverter;
import com.example.server.converters.OfferDtoGrpcConverter;
import com.example.server.dto.offer.CreateOfferRequestDto;
import com.example.server.dto.offer.OfferResponseDto;
import com.example.server.dto.offer.ShortOfferResponseDto;
import com.example.server.dto.offer.UpdateOfferRequestDto;
import com.example.shared.converters.StringToTimestampConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.server.converters.OfferDtoGrpcConverter.*;

@Service public class OfferService {
  private final DataServerStub dataServerStub;
  private final ImageStorageService imageStorageService;

  @Autowired public OfferService(DataServerStub dataServerStub,
      ImageStorageService imageStorageService) {
    this.dataServerStub = dataServerStub;
    this.imageStorageService = imageStorageService;

    System.out.println("OfferService created");
  }

  @Transactional public OfferResponseDto saveOffer(
      CreateOfferRequestDto offerRequestDto) {

    if (!isPickupInFuture(offerRequestDto.getPickupTimeStart()))
      throw new IllegalArgumentException("Pickup time must be in the future");

    String imagePath = null;
    try {
      imagePath = imageStorageService.getBaseDirectory()
          + imageStorageService.saveImage(offerRequestDto.getImage());
      //Transform the dto to grpc message
      SaveOfferRequest request = CreateOfferRequestDto_To_SaveOfferRequest(
          offerRequestDto, imagePath);

      //Send the request to the data server
      SaveOfferResponse response = dataServerStub.saveOffer(request);

      //Return the offer as dto
      return SaveOfferResponseGrpc_To_OfferResponseDto(response);
    }
    catch (IOException e) {
      e.printStackTrace();
      if (imagePath != null)
        imageStorageService.deleteImage(imagePath); //rollback
      throw new IllegalArgumentException("Failed to save the image");
    }

  }

  public OfferResponseDto getOfferById(String id) {
    System.out.println("getOfferById method called with id: " + id);
    OfferIdRequest request = OfferIdRequest.newBuilder().setId(id).build();
    OfferResponse response = dataServerStub.getOfferById(request);

    System.out.println("Received response from dataServerStub: " + response);

    return OfferDtoGrpcConverter.OfferResponseGrpc_To_OfferResponseDto(
        response);
  }

  public List<ShortOfferResponseDto> getAvailableOffers() {
    //Build the grpc request
    EmptyMessage request = EmptyMessage.newBuilder().build();

    //Send the request to the data server
    OfferList response = dataServerStub.getAvailableOffers(request);

    ArrayList<ShortOfferResponseDto> offers = new ArrayList<>();
    for (int i = 0; i < response.getOfferCount(); i++) {
      ShortOfferResponseDto dto = OfferDtoGrpcConverter.ShortOfferResponseGrpc_To_ShortOfferResponseDto(
          response.getOffer(i));

      //Attach the images
      String imagePath = response.getOffer(i).getImagePath();
      if (imageExists(imagePath))
        dto.setImagePath(imagePath);
      else
        dto.setImagePath(null);
      //        throw new IllegalArgumentException(
      //            "Image not found for order ID: " + response.getOffer(i).getId()
      //                + " with path " + imagePath);
      offers.add(dto);
    }

    return offers;
  }

  public OfferResponseDto updateOffer(
      UpdateOfferRequestDto updateOfferRequestDto) {
    //First check what we couldn't check in the Dto class
    if (!isPickupInFuture(updateOfferRequestDto.getPickupTimeStart()))
      throw new IllegalArgumentException("Pickup time must be in the future");

    String imagePath = null;
    try {
      imagePath = imageStorageService.getBaseDirectory()
          + imageStorageService.saveImage(updateOfferRequestDto.getImage());
      //Transform the dto to grpc message
      OfferResponse request = UpdateOfferRequestDto_To_OfferResponse(
          updateOfferRequestDto, imagePath);

      //Send the request to the data server
      OfferResponse response = dataServerStub.updateOffer(request);

      //Return the offer as dto
      return OfferResponseGrpc_To_OfferResponseDto(response);
    }
    catch (IOException e) {
      e.printStackTrace();
      if (imagePath != null)
        imageStorageService.deleteImage(imagePath); //rollback
      throw new IllegalArgumentException("Failed to save the image");
    }

  }

  private boolean isPickupInFuture(LocalDateTime time) {
    return time.isAfter(LocalDateTime.now());
  }

  public boolean imageExists(String filePath) {
    File imageFile = new File(filePath);
    return imageFile.exists();
  }

  public List<ShortOfferResponseDto> getFilteredOffers(
      Optional<Integer> minOfferPrice, Optional<Integer> maxOfferPrice,
      Optional<String> pickupTimeStart, Optional<String> pickupTimeEnd,
      Optional<List<String>> categories) {
    var stringConverter = new StringToTimestampConverter();
    var req = FilterRequest.newBuilder();

    if (minOfferPrice.isPresent()) {
      req.setMinOfferPrice(minOfferPrice.get());
    }

    if (maxOfferPrice.isPresent()) {
      req.setMaxOfferPrice(maxOfferPrice.get());
    }

    if (categories.isPresent()) {
      req.addAllCategories(categories.get());
    }

    if (pickupTimeStart.isPresent()) {
      req.setPickupTimeStart(stringConverter.convert(pickupTimeStart.get()));
    }

    if (pickupTimeEnd.isPresent()) {
      req.setPickupTimeEnd(stringConverter.convert(pickupTimeEnd.get()));
    }

    FullOfferList fullResponse = dataServerStub.getOffers(req.build());
    return FullOfferListToShortOfferListConverter.convert(fullResponse);
//    return response.getOfferList().stream()
//        .map(OfferDtoGrpcConverter::OfferResponseGrpc_To_OfferResponseDto).toList();
  }
}

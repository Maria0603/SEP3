package com.example.server.services.Implementations;

import com.example.sep3.grpc.*;
import com.example.server.DataServerStub;
import com.example.server.converters.OfferDtoGrpcConverter;
import com.example.server.dto.offer.CreateOfferRequestDto;
import com.example.server.dto.offer.OfferResponseDto;
import com.example.server.dto.offer.UpdateOfferRequestDto;
import com.example.server.services.auxServices.IImageStorageService;
import com.example.server.services.auxServices.Implementations.AzureBlobStorageService;
//import com.example.server.services.auxServices.Implementations.ImageStorageStorageService;
import com.example.shared.converters.StringToTimestampConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.server.services.IOfferService;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.server.converters.OfferDtoGrpcConverter.*;

@Service public class OfferService implements IOfferService
{
  private final DataServerStub dataServerStub;
  private final IImageStorageService imageStorageService;

  @Autowired public OfferService(DataServerStub dataServerStub,
                                 IImageStorageService imageStorageService)
  {
    this.dataServerStub = dataServerStub;
    this.imageStorageService = imageStorageService;

    System.out.println("OfferService created");
  }

  @Transactional @Override public OfferResponseDto createOffer(
      CreateOfferRequestDto offerRequestDto, String userId)
  {

    if (!isPickupInFuture(offerRequestDto.getPickupTimeStart()))
      throw new IllegalArgumentException("Pickup time must be in the future");

    String imagePath = null;
    try
    {
      imagePath = imageStorageService.saveImage(offerRequestDto.getImage());
      //Transform the dto to grpc message
      CreateOfferRequest request = CreateOfferRequestDto_To_SaveOfferRequest(
          offerRequestDto, imagePath, userId);

      //Send the request to the data server
      OfferResponse response = dataServerStub.createOffer(request);

      //Return the offer as dto
      return OfferResponseGrpc_To_OfferResponseDto(response);
    }
    catch (IOException e)
    {
      e.printStackTrace();
      if (imagePath != null)
        imageStorageService.deleteImage(imagePath); //rollback
      throw new IllegalArgumentException("Failed to save the image");
    }

  }

  @Override public OfferResponseDto getOfferById(String id)
  {
    System.out.println("getOfferById method called with id: " + id);
    OfferIdRequest request = OfferIdRequest.newBuilder().setId(id).build();
    OfferResponse response = dataServerStub.getOfferById(request);

    System.out.println("Received response from dataServerStub: " + response);

    return OfferDtoGrpcConverter.OfferResponseGrpc_To_OfferResponseDto(
        response);
  }

//  @Override
//  public List<OfferResponseDto> getOffers(Optional<Integer> minOfferPrice, Optional<Integer> maxOfferPrice, Optional<String> pickupTimeStart, Optional<String> pickupTimeEnd, Optional<List<String>> categories, String userId) {
//    return List.of();
//  }
  @Override
  public List<OfferResponseDto> getOffers(Optional<Integer> minOfferPrice,
      Optional<Integer> maxOfferPrice, Optional<String> pickupTimeStart,
      Optional<String> pickupTimeEnd, Optional<List<String>> categories,
                                          Optional<String> userId)
  {
    var stringConverter = new StringToTimestampConverter();
    var req = FilterRequest.newBuilder();

    minOfferPrice.ifPresent(req::setMinOfferPrice);
    maxOfferPrice.ifPresent(req::setMaxOfferPrice);
    categories.ifPresent(req::addAllCategories);
    pickupTimeStart.ifPresent(
        s -> req.setPickupTimeStart(stringConverter.convert(s)));
    pickupTimeEnd.ifPresent(
        s -> req.setPickupTimeEnd(stringConverter.convert(s)));

    userId.ifPresent(req::setUserId);

    OfferListResponse fullResponse = dataServerStub.getOffers(req.build());
    return OfferDtoGrpcConverter.OfferListResponse_To_ListOfferResponseDto(fullResponse);
    //    return response.getOfferList().stream()
    //        .map(OfferDtoGrpcConverter::OfferResponseGrpc_To_OfferResponseDto).toList();
  }


  @Override public OfferResponseDto updateOffer(
      UpdateOfferRequestDto updateOfferRequestDto)
  {
    //First check what we couldn't check in the Dto class
    if (!isPickupInFuture(updateOfferRequestDto.getPickupTimeStart()))
      throw new IllegalArgumentException("Pickup time must be in the future");

    String imagePath = null;
    try
    {
      imagePath = imageStorageService.saveImage(updateOfferRequestDto.getImage());
      //Transform the dto to grpc message
      OfferResponse request = UpdateOfferRequestDto_To_OfferResponse(
          updateOfferRequestDto, imagePath);

      //Send the request to the data server
      OfferResponse response = dataServerStub.updateOffer(request);

      //Return the offer as dto
      return OfferResponseGrpc_To_OfferResponseDto(response);
    }
    catch (IOException e)
    {
      e.printStackTrace();
      if (imagePath != null)
        imageStorageService.deleteImage(imagePath); //rollback
      throw new IllegalArgumentException("Failed to save the image");
    }

  }

  private boolean isPickupInFuture(LocalDateTime time)
  {
    return time.isAfter(LocalDateTime.now());
  }

//  public boolean imageExists(String filePath)
//  {
//    File imageFile = new File(filePath);
//    return imageFile.exists();
//  }



    //   minOfferPrice.ifPresent(req::setMinOfferPrice);

    //   maxOfferPrice.ifPresent(req::setMaxOfferPrice);

    //   categories.ifPresent(req::addAllCategories);

    //   pickupTimeStart.ifPresent(s -> req.setPickupTimeStart(stringConverter.convert(s)));

    //   pickupTimeEnd.ifPresent(s -> req.setPickupTimeEnd(stringConverter.convert(s)));

    // FullOfferList fullResponse = dataServerStub.getOffers(req.build());
    // return FullOfferListToShortOfferListConverter.convert(fullResponse);
  }

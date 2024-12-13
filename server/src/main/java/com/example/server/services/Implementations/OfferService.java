package com.example.server.services.Implementations;

import com.example.sep3.grpc.*;
import com.example.server.DataServerStub;
import com.example.server.converters.OfferDtoGrpcConverter;
import com.example.server.dto.offer.CreateOfferRequestDto;
import com.example.server.dto.offer.FilterRequestDto;
import com.example.server.dto.offer.OfferResponseDto;
import com.example.server.dto.offer.UpdateOfferRequestDto;
import com.example.server.services.auxServices.IImageStorageService;
import com.example.server.services.auxServices.Implementations.AzureBlobStorageService;
import com.example.server.services.auxServices.Implementations.ImageStorageStorageService;
import com.example.shared.converters.StringToTimestampConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.server.services.IOfferService;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
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

  // Helper method to convert LocalDateTime to gRPC Timestamp
//  private Timestamp convertToTimestamp(LocalDateTime dateTime) {
//    return Timestamp.newBuilder()
//            .setSeconds(dateTime.toEpochSecond(ZoneOffset.UTC))
//            .setNanos(dateTime.getNano())
//            .build();
//  }

  @Override public OfferResponseDto getOfferById(String id)
  {

    OfferIdRequest request = OfferIdRequest.newBuilder().setId(id).build();
    OfferResponse response = dataServerStub.getOfferById(request);

    return OfferDtoGrpcConverter.OfferResponseGrpc_To_OfferResponseDto(
        response);
  }

//  @Override
//  public List<OfferResponseDto> getOffers(Optional<Integer> minOfferPrice, Optional<Integer> maxOfferPrice, Optional<String> pickupTimeStart, Optional<String> pickupTimeEnd, Optional<List<String>> categories, String userId) {
//    return List.of();
//  }
//  @Override
//  public List<OfferResponseDto> getOffers(Optional<Integer> minOfferPrice,
//      Optional<Integer> maxOfferPrice, Optional<String> pickupTimeStart,
//      Optional<String> pickupTimeEnd, Optional<List<String>> categories,
//                                          Optional<String> userId)
//  {
//    var stringConverter = new StringToTimestampConverter();
//    var req = FilterRequest.newBuilder();
//
//    minOfferPrice.ifPresent(req::setMinOfferPrice);
//    maxOfferPrice.ifPresent(req::setMaxOfferPrice);
//    categories.ifPresent(req::addAllCategories);
//    pickupTimeStart.ifPresent(
//        s -> req.setPickupTimeStart(stringConverter.convert(s)));
//    pickupTimeEnd.ifPresent(
//        s -> req.setPickupTimeEnd(stringConverter.convert(s)));
//
//    userId.ifPresent(req::setUserId);
//
//    OfferListResponse fullResponse = dataServerStub.getOffers(req.build());
//    return OfferDtoGrpcConverter.OfferListResponse_To_ListOfferResponseDto(fullResponse);
//    //    return response.getOfferList().stream()
//    //        .map(OfferDtoGrpcConverter::OfferResponseGrpc_To_OfferResponseDto).toList();
//  }

@Override
public List<OfferResponseDto> getOffers(FilterRequestDto filterRequestDto) {
  var req = FilterRequest.newBuilder();

  // Populate fields directly from FilterRequestDto
  Optional.ofNullable(filterRequestDto.getMinOfferPrice()).ifPresent(req::setMinOfferPrice);
  Optional.ofNullable(filterRequestDto.getMaxOfferPrice()).ifPresent(req::setMaxOfferPrice);
  Optional.ofNullable(filterRequestDto.getCategories()).ifPresent(req::addAllCategories);
  Optional.ofNullable(filterRequestDto.getPickupTimeStart()).ifPresent(
          start -> req.setPickupTimeStart(
                  com.google.protobuf.Timestamp.newBuilder()
                          .setSeconds(start.toEpochSecond(java.time.ZoneOffset.UTC))
                          .setNanos(start.getNano())
                          .build())
  );
  Optional.ofNullable(filterRequestDto.getPickupTimeEnd()).ifPresent(
          end -> req.setPickupTimeEnd(
                  com.google.protobuf.Timestamp.newBuilder()
                          .setSeconds(end.toEpochSecond(java.time.ZoneOffset.UTC))
                          .setNanos(end.getNano())
                          .build())
  );

  // Handle location if present
  Optional.ofNullable(filterRequestDto.getLocation()).ifPresent(location -> {
    var locationBuilder = Location.newBuilder();
    Optional.ofNullable(location.getLatitude()).ifPresent(locationBuilder::setLatitude);
    Optional.ofNullable(location.getLongitude()).ifPresent(locationBuilder::setLongitude);
    Optional.ofNullable(location.getRadius()).ifPresent(locationBuilder::setRadius);
    req.setLocation(locationBuilder.build());
  });

  // Send gRPC request
  OfferListResponse fullResponse = dataServerStub.getOffers(req.build());

  // Convert gRPC response to DTOs
  return OfferDtoGrpcConverter.OfferListResponse_To_ListOfferResponseDto(fullResponse);
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

package com.example.server.converters;

import com.example.sep3.grpc.*;
import com.example.server.dto.CreateOfferRequestDto;
import com.example.server.dto.OfferResponseDto;

public class DtoGrpcConverter
{
  public static SaveOfferRequest CreateOfferRequestDto_To_SaveOfferRequest(
      CreateOfferRequestDto createOfferRequestDto, String imagePath)
  {
    // Create SaveOfferRequest builder
    SaveOfferRequest.Builder saveOfferRequestBuilder = SaveOfferRequest.newBuilder();

    // Mapping simple fields
    saveOfferRequestBuilder.setTitle(createOfferRequestDto.getTitle());
    saveOfferRequestBuilder.setDescription(
        createOfferRequestDto.getDescription());
    saveOfferRequestBuilder.setOriginalPrice(
        createOfferRequestDto.getOriginalPrice());
    saveOfferRequestBuilder.setOfferPrice(
        createOfferRequestDto.getOfferPrice());
    saveOfferRequestBuilder.setNumberOfItems(
        createOfferRequestDto.getNumberOfItems());
    saveOfferRequestBuilder.addAllCategories(
        createOfferRequestDto.getCategories());
    saveOfferRequestBuilder.setImagePath(imagePath);

    // Convert DateDto to Date
    Date pickupDate = DateConverter.convertDateDtoToGrpcDate(
        createOfferRequestDto.getPickupDate());
    saveOfferRequestBuilder.setPickupDate(pickupDate);

    // Convert TimeDto to Time for start and end time
    Time pickupTimeStart = TimeConverter.convertTimeDtoToGrpcTime(
        createOfferRequestDto.getPickupTimeStart());
    saveOfferRequestBuilder.setPickupTimeStart(pickupTimeStart);

    Time pickupTimeEnd = TimeConverter.convertTimeDtoToGrpcTime(
        createOfferRequestDto.getPickupTimeEnd());
    saveOfferRequestBuilder.setPickupTimeEnd(pickupTimeEnd);

    // Build and return the SaveOfferRequest object
    return saveOfferRequestBuilder.build();
  }

  public static OfferResponseDto OfferResponseGrpc_To_OfferResponseDto(
      OfferResponse offerResponseGrpc)
  {
    OfferResponseDto dto = new OfferResponseDto();
    dto.setId(offerResponseGrpc.getId());
    dto.setTitle(offerResponseGrpc.getTitle());
    dto.setDescription(offerResponseGrpc.getDescription());
    dto.setOriginalPrice(offerResponseGrpc.getOriginalPrice());
    dto.setOfferPrice(offerResponseGrpc.getOfferPrice());
    dto.setNumberOfItems(offerResponseGrpc.getNumberOfItems());
    dto.setCategories(offerResponseGrpc.getCategoriesList().stream().toList());
    dto.setPickupDate(DateConverter.convertGrpcDateToDateDto(
        offerResponseGrpc.getPickupDate()));
    dto.setPickupTimeStart(TimeConverter.convertGrpcTimeToTimeDto(
        offerResponseGrpc.getPickupTimeStart()));
    dto.setPickupTimeEnd(TimeConverter.convertGrpcTimeToTimeDto(
        offerResponseGrpc.getPickupTimeEnd()));
    dto.setStatus(offerResponseGrpc.getStatus());

    return dto;
  }
}

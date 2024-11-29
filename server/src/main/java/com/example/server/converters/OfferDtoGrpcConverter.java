package com.example.server.converters;

import com.example.sep3.grpc.*;
import com.example.server.dto.offer.CreateOfferRequestDto;
import com.example.server.dto.offer.OfferResponseDto;
import com.example.server.dto.offer.ShortOfferResponseDto;
import com.example.server.dto.offer.UpdateOfferRequestDto;

public class OfferDtoGrpcConverter
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
    saveOfferRequestBuilder.setNumberOfAvailableItems(
        createOfferRequestDto.getNumberOfItems()); //all items are available
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

  public static OfferResponse UpdateOfferRequestDto_To_OfferResponse(
      UpdateOfferRequestDto updateOfferRequestDto, String imagePath)
  {
    OfferResponse.Builder offerResponseBuilder = OfferResponse.newBuilder();

    // Mapping simple fields
    offerResponseBuilder.setId(updateOfferRequestDto.getId());
    offerResponseBuilder.setTitle(updateOfferRequestDto.getTitle());
    offerResponseBuilder.setDescription(updateOfferRequestDto.getDescription());
    offerResponseBuilder.setOriginalPrice(
        updateOfferRequestDto.getOriginalPrice());
    offerResponseBuilder.setOfferPrice(updateOfferRequestDto.getOfferPrice());
    offerResponseBuilder.setNumberOfItems(
        updateOfferRequestDto.getNumberOfItems());
    offerResponseBuilder.setNumberOfAvailableItems(
        updateOfferRequestDto.getNumberOfAvailableItems());
    offerResponseBuilder.addAllCategories(
        updateOfferRequestDto.getCategories());
    offerResponseBuilder.setImagePath(imagePath);
    offerResponseBuilder.setStatus(updateOfferRequestDto.getStatus());

    // Convert DateDto to Date
    Date pickupDate = DateConverter.convertDateDtoToGrpcDate(
        updateOfferRequestDto.getPickupDate());
    offerResponseBuilder.setPickupDate(pickupDate);

    // Convert TimeDto to Time for start and end time
    Time pickupTimeStart = TimeConverter.convertTimeDtoToGrpcTime(
        updateOfferRequestDto.getPickupTimeStart());
    offerResponseBuilder.setPickupTimeStart(pickupTimeStart);

    Time pickupTimeEnd = TimeConverter.convertTimeDtoToGrpcTime(
        updateOfferRequestDto.getPickupTimeEnd());
    offerResponseBuilder.setPickupTimeEnd(pickupTimeEnd);

    // Build and return the SaveOfferRequest object
    return offerResponseBuilder.build();
  }

  public static OfferResponseDto SaveOfferResponseGrpc_To_OfferResponseDto(
      SaveOfferResponse saveOfferResponseGrpc)
  {
    OfferResponseDto dto = new OfferResponseDto();
    dto.setId(saveOfferResponseGrpc.getId());
    dto.setTitle(saveOfferResponseGrpc.getTitle());
    dto.setOriginalPrice(saveOfferResponseGrpc.getOriginalPrice());
    dto.setOfferPrice(saveOfferResponseGrpc.getOfferPrice());
    dto.setNumberOfItems(saveOfferResponseGrpc.getNumberOfItems());
    dto.setPickupDate(DateConverter.convertGrpcDateToDateDto(
        saveOfferResponseGrpc.getPickupDate()));
    dto.setPickupTimeStart(TimeConverter.convertGrpcTimeToTimeDto(
        saveOfferResponseGrpc.getPickupTimeStart()));
    dto.setPickupTimeEnd(TimeConverter.convertGrpcTimeToTimeDto(
        saveOfferResponseGrpc.getPickupTimeEnd()));
    dto.setCategories(saveOfferResponseGrpc.getCategoriesList());
    dto.setDescription(saveOfferResponseGrpc.getDescription());
    dto.setImagePath(saveOfferResponseGrpc.getImagePath());
    dto.setStatus("available");// maybe not send it at all
    return dto;
  }

  public static ShortOfferResponseDto ShortOfferResponseGrpc_To_ShortOfferResponseDto(
      ShortOfferResponse offerResponseGrpc)
  {
    ShortOfferResponseDto dto = new ShortOfferResponseDto();
    dto.setId(offerResponseGrpc.getId());
    dto.setTitle(offerResponseGrpc.getTitle());
    dto.setOriginalPrice(offerResponseGrpc.getOriginalPrice());
    dto.setOfferPrice(offerResponseGrpc.getOfferPrice());
    dto.setNumberOfAvailableItems(
        offerResponseGrpc.getNumberOfAvailableItems());
    dto.setPickupDate(DateConverter.convertGrpcDateToDateDto(
        offerResponseGrpc.getPickupDate()));
    dto.setPickupTimeStart(TimeConverter.convertGrpcTimeToTimeDto(
        offerResponseGrpc.getPickupTimeStart()));
    dto.setPickupTimeEnd(TimeConverter.convertGrpcTimeToTimeDto(
        offerResponseGrpc.getPickupTimeEnd()));
    dto.setStatus(offerResponseGrpc.getStatus());

    return dto;
  }


  public static OfferResponseDto OfferResponseGrpc_To_OfferResponseDto(
      OfferResponse response)
  {
    OfferResponseDto dto = new OfferResponseDto();

    dto.setId(response.getId());
    dto.setTitle(response.getTitle());
    dto.setOriginalPrice(response.getOriginalPrice());
    dto.setOfferPrice(response.getOfferPrice());
    dto.setNumberOfItems(response.getNumberOfItems());
    dto.setNumberOfAvailableItems(response.getNumberOfAvailableItems());
    dto.setPickupDate(
        DateConverter.convertGrpcDateToDateDto(response.getPickupDate()));
    dto.setPickupTimeStart(
        TimeConverter.convertGrpcTimeToTimeDto(response.getPickupTimeStart()));
    dto.setPickupTimeEnd(
        TimeConverter.convertGrpcTimeToTimeDto(response.getPickupTimeEnd()));
    dto.setCategories(response.getCategoriesList());
    dto.setDescription(response.getDescription());
    dto.setImagePath(response.getImagePath());
    dto.setStatus(response.getStatus());

    return dto;
  }

  public static UpdateOfferRequestDto OfferResponseDto_To_UpdateOfferRequestDto(OfferResponseDto offerResponseDto, byte[] image)
  {
    UpdateOfferRequestDto dto = new UpdateOfferRequestDto();

    dto.setId(offerResponseDto.getId());
    dto.setTitle(offerResponseDto.getTitle());
    dto.setOriginalPrice(offerResponseDto.getOriginalPrice());
    dto.setOfferPrice(offerResponseDto.getOfferPrice());
    dto.setNumberOfItems(offerResponseDto.getNumberOfItems());
    dto.setNumberOfAvailableItems(offerResponseDto.getNumberOfAvailableItems());
    dto.setPickupDate(offerResponseDto.getPickupDate());
    dto.setPickupTimeStart(offerResponseDto.getPickupTimeStart());
    dto.setPickupTimeEnd(offerResponseDto.getPickupTimeEnd());
    dto.setCategories(offerResponseDto.getCategories());
    dto.setDescription(offerResponseDto.getDescription());
    dto.setImage(image);
    dto.setStatus(offerResponseDto.getStatus());

    return dto;
  }
}

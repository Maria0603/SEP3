package com.example.server.converters;

import com.example.sep3.grpc.*;
import com.example.server.dto.offer.CreateOfferRequestDto;
import com.example.server.dto.offer.OfferResponseDto;
import com.example.server.dto.offer.ShortOfferResponseDto;
import com.example.server.dto.order.AddOrderRequestDto;
import com.example.server.dto.order.OrderResponseDto;

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

  public static AddOrderRequest AddOrderRequestDto_To_AddOrderRequest(
      AddOrderRequestDto addOrderRequestDto)
  {
    return AddOrderRequest.newBuilder()
        .setOfferId(addOrderRequestDto.getOfferId())
        .setNumberOfItems(addOrderRequestDto.getNumberOfItems())
        .setUserId(addOrderRequestDto.getUserId()).build();
  }

  public static OrderResponseDto OrderResponseGrpc_To_OrderResponseDto(
      OrderResponse orderResponse)
  {
    OrderResponseDto dto = new OrderResponseDto();
    dto.setOrderId(orderResponse.getId());
    dto.setUserId(orderResponse.getUserId());
    dto.setOfferId(orderResponse.getOfferId());
    dto.setNumberOfItems(orderResponse.getNumberOfItems());
    dto.setStatus(orderResponse.getStatus());
    dto.setOrderDate(
        DateConverter.convertGrpcDateToDateDto(orderResponse.getOrderDate()));
    dto.setOrderTime(
        TimeConverter.convertGrpcTimeToTimeDto(orderResponse.getOrderTime()));
    dto.setPricePerItem(orderResponse.getPricePerItem());
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
}

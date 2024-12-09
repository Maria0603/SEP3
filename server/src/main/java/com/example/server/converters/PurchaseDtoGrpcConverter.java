package com.example.server.converters;

import com.example.sep3.grpc.CreatePurchaseRequest;
import com.example.sep3.grpc.PurchaseResponse;
import com.example.server.dto.purchase.PurchaseResponseDto;
import com.example.server.dto.purchase.CreatePurchaseRequestDto;
import com.example.shared.converters.DateTimeConverter;

public class PurchaseDtoGrpcConverter
{

  public static CreatePurchaseRequest CreatePurchaseRequestDto_To_CreatePurchaseRequest(
      CreatePurchaseRequestDto requestDto, String userId)
  {
    return CreatePurchaseRequest.newBuilder()
        .setOfferId(requestDto.getOfferId())
        .setNumberOfItems(requestDto.getNumberOfItems())
        .setCustomerId(userId).build();
  }

  public static PurchaseResponseDto PurchaseResponse_To_PurchaseResponseDto(
      PurchaseResponse response)
  {
    PurchaseResponseDto dto = new PurchaseResponseDto();
    dto.setPurchaseId(response.getId());
    dto.setCustomerId(response.getCustomerId());
    dto.setOfferId(response.getOfferId());
    dto.setNumberOfItems(response.getNumberOfItems());
    dto.setStatus(response.getStatus());

    dto.setPurchaseTime(DateTimeConverter.convertProtoTimestamp_To_LocalDateTime(response.getPurchaseTime()));
    dto.setPricePerItem(response.getPricePerItem());
    return dto;
  }
}

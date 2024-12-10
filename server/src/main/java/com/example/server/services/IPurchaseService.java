package com.example.server.services;

import com.example.sep3.grpc.PurchaseListResponse;
import com.example.server.dto.purchase.PurchaseResponseDto;
import com.example.server.dto.purchase.CreatePurchaseRequestDto;
import com.example.server.dto.purchase.CreatePurchaseSessionResponseDto;

import java.util.List;

public interface IPurchaseService
{
  CreatePurchaseSessionResponseDto createPurchase(
      CreatePurchaseRequestDto requestDto,
      String userId);
  void refinePurchase(String payload, String sigHeader);
  List<PurchaseResponseDto> getAllPurchases(String userId);
  PurchaseResponseDto getPurchaseById(String id);
  List<PurchaseResponseDto> getPurchasesByCustomerId(String customerId);
}

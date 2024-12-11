package com.example.server.dto.purchase;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor public class PurchaseResponseDto
{

  @NotNull(message = "Purchase ID is required.") private String purchaseId;

  @NotNull(message = "Customer ID is required.") private String customerId;

  @NotNull(message = "Offer ID is required.") private String offerId;

  @NotNull(message = "Quantity is required.") private int numberOfItems;

  @NotNull(message = "Status is required.") private String status;

  @NotNull(message = "Purchase time is required.") private LocalDateTime purchaseTime;

  @NotNull(message = "Price per item is required.") private int pricePerItem;
  private String businessName;
  private String businessEmail;
  private String customerEmail;
}
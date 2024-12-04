package com.example.server.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor public class OrderResponseDto
{

  @NotNull(message = "Order ID is required.") private String orderId;

  @NotNull(message = "User ID is required.") private String userId;

  @NotNull(message = "Offer ID is required.") private String offerId;

  @NotNull(message = "Quantity is required.") private int numberOfItems;

  @NotNull(message = "Status is required.") private String status;

  @NotNull(message = "Order time is required.") private LocalDateTime orderTime;

  @NotNull(message = "Price per item is required.") private int pricePerItem;

}
package com.example.server.dto.purchase;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor public class DetailedPurchaseResponseDto
{

  @NotNull(message = "Purchase ID is required.") private String purchaseId;

  @NotNull(message = "Title is required.") private String title;

  @NotNull(message = "Description is required.") private String description;

  @NotNull(message = "Quantity is required.") private int numberOfPurchasedItems;

  @NotNull(message = "Image path is required.") private String imagePath;

  @NotNull(message = "Pickup date is required.") private LocalDateTime pickupDate;
  @NotNull(message = "Pickup time start is required.") private LocalDateTime pickupTimeStart;
  @NotNull(message = "Pickup time end is required.") private LocalDateTime pickupTimeEnd;
  @NotNull(message = "Customer first name is required.") private String customerFirstName;
  @NotNull(message = "Customer last name is required.") private String customerLastName;
  @NotNull(message = "Customer email is required.") private String customerEmail;
  @NotNull(message = "Customer phone number is required.") private String customerPhoneNumber;
  @NotNull(message = "Purchase time is required.") private LocalDateTime purchaseTime;

}
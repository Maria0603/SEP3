package com.example.server.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor public class AddOrderRequestDto
{

  @NotNull(message = "User ID is required.") private String userId;

  @NotNull(message = "Offer ID is required.") private String offerId;

  @NotNull(message = "Quantity is required.") private int numberOfItems;

}
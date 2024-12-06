package com.example.server.dto.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor public class PlaceOrderRequestDto
{
  @NotNull(message = "Invalid offer ID.") private String offerId;

  @NotNull(message = "Invalid number of items.") @Size(min = 1, message = "You must select at least one item.") private int numberOfItems;

  // @NotNull(message = "User ID is required.") private String userId;

}


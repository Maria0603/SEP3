package com.example.server.dto.purchase;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor public class CreatePurchaseRequestDto
{
  @NotNull(message = "Invalid offer ID.") private String offerId;

  @NotNull(message = "Invalid number of items.") @Size(min = 1, message = "You must select at least one item.") private int numberOfItems;
}


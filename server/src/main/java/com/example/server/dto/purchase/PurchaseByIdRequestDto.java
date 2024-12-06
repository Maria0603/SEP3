package com.example.server.dto.purchase;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor public class PurchaseByIdRequestDto
{

  @NotNull(message = "Purchase ID is required.") private String purchaseId;
}
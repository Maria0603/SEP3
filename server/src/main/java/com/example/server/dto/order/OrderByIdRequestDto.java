package com.example.server.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor public class OrderByIdRequestDto
{

  @NotNull(message = "Order ID is required.") private String orderId;
}
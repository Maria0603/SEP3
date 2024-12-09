package com.example.server.dto.purchase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor public class CreatePurchaseSessionResponseDto
{
  private String sessionId;
  private String url;
}

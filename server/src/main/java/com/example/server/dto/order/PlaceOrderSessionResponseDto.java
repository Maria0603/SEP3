package com.example.server.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor public class PlaceOrderSessionResponseDto
{
  private String sessionId;
  private String url;
}

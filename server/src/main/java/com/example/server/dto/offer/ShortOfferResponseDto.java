package com.example.server.dto.offer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortOfferResponseDto
{

  private String id;
  private String title;
  private int originalPrice;
  private int offerPrice;
  private int numberOfAvailableItems;
  private LocalDateTime pickupTimeStart;
  private LocalDateTime pickupTimeEnd;
  private String imagePath;
  private String status;

}


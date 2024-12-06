package com.example.server.dto.offer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferResponseDto
{

  private String id;
  private String title;
  private String description;
  private int originalPrice;
  private int offerPrice;
  private int numberOfItems;
  private int numberOfAvailableItems;
  private LocalDateTime pickupTimeStart;
  private LocalDateTime pickupTimeEnd;
  private List<String> categories;
  private String imagePath;
  private String status;
  private String businessId;
  private String businessName;
  private String businessLogoPath;

}

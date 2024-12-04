package com.example.server.dto.business;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessesInRadiusResponseDto
{
  private String businessName;
  private String businessId;
  private String businessEmail;
  private String logoPath;
  private double latitude;
  private double longitude;
}

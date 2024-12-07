package com.example.server.dto.customer;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor public class CustomerLocationRequestDto
{
  @NotNull(message = "Latitude is required.") @Min(value = -90, message = "Latitude must be between -90 and 90.") @Max(value = 90, message = "Latitude must be between -90 and 90.") private double latitude;

  @NotNull(message = "Longitude is required.") @Min(value = -180, message = "Longitude must be between -180 and 180.") @Max(value = 180, message = "Longitude must be between -180 and 180.") private double longitude;

  @NotNull(message = "Radius is required.") @Min(value = 1, message = "Radius must be at least 1 km.") @Max(value = 100, message = "Radius cannot exceed 100 km.") private int radius;
}


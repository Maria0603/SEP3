package com.example.server.dto.offer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LocationDto {
    private Double longitude;
    private Double latitude;
    private Double radius;
}

package com.example.server.dto.offer;
import lombok.Data;

@Data
public class LocationDto {
    private Integer radius;
    private Double longitude;
    private Double latitude;
}

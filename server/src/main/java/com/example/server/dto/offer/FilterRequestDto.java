package com.example.server.dto.offer;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FilterRequestDto {
    private Integer minOfferPrice;
    private Integer maxOfferPrice;
    private LocalDateTime pickupTimeStart;
    private LocalDateTime pickupTimeEnd;
    private List<String> categories;
    private LocationDto location;
}

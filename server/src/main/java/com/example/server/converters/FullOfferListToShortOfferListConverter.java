package com.example.server.converters;

import com.example.server.dto.offer.ShortOfferResponseDto;
import com.example.sep3.grpc.FullOfferList;
import com.example.shared.converters.DateTimeConverter;

import java.util.List;
import java.util.stream.Collectors;

public class FullOfferListToShortOfferListConverter {

    public static List<ShortOfferResponseDto> convert(FullOfferList fullOfferList) {
        return fullOfferList.getOfferList().stream()
                .map(offer -> ShortOfferResponseDto.builder()
                        .id(offer.getId())
                        .title(offer.getTitle())
                        .originalPrice(offer.getOriginalPrice())
                        .offerPrice(offer.getOfferPrice())
                        .numberOfAvailableItems(offer.getNumberOfAvailableItems())
                        .pickupTimeStart(DateTimeConverter.convertProtoTimestamp_To_LocalDateTime(offer.getPickupTimeStart()))
                        .pickupTimeEnd(DateTimeConverter.convertProtoTimestamp_To_LocalDateTime(offer.getPickupTimeEnd()))
                        .imagePath(offer.getImagePath())
                        .status(offer.getStatus())
                        .build())
                .collect(Collectors.toList());
    }
}
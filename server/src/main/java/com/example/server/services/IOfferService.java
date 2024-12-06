package com.example.server.services;

import com.example.server.dto.offer.CreateOfferRequestDto;
import com.example.server.dto.offer.OfferResponseDto;
import com.example.server.dto.offer.UpdateOfferRequestDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface IOfferService
{
  @Transactional OfferResponseDto createOffer(
      CreateOfferRequestDto offerRequestDto, String userId);
  OfferResponseDto getOfferById(String id);
  List<OfferResponseDto> getOffers(Optional<Integer> minOfferPrice,
      Optional<Integer> maxOfferPrice, Optional<String> pickupTimeStart,
      Optional<String> pickupTimeEnd, Optional<List<String>> categories,
      String userId);
  OfferResponseDto updateOffer(UpdateOfferRequestDto updateOfferRequestDto);
}

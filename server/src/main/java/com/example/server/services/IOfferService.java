package com.example.server.services;

import com.example.server.dto.offer.CreateOfferRequestDto;
import com.example.server.dto.offer.FilterRequestDto;
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
  List<OfferResponseDto> getOffers(FilterRequestDto filterRequestDto);
  List<OfferResponseDto> getOffersByBusinessId(String businessId);
}

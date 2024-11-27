﻿using client.DTO;
using client.Pages;

namespace client.Services;

public interface IOfferService
{
    public Task<OfferResponseDto> SaveOfferAsync(CreateOfferRequestDto createOfferRequestDto);
    public Task<List<ShortOfferResponseDto>> GetOffersAsync();
    public Task<OfferResponseDto> GetOfferByIdAsync(string id);
}
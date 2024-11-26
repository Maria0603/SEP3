﻿using client.DTO;
using client.Pages;

namespace client.Services;

public interface IOfferService
{
    public Task<OfferResponseDto> SaveOfferAsync(CreateOfferRequestDto createOfferRequestDto);
    public Task<List<Models.Offer>> GetOffersAsync();
    public Task<string> GetOffersJsonAsync();
}
﻿using client.DTO;
using client.DTO.Purchase;
using client.Pages;

namespace client.Services;

public interface IPurchaseService
{
    public Task<PurchaseResponseDto> CreatePurchaseAsync(PurchaseRequestDto purchaseRequestDto);
}
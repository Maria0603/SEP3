﻿using System.Net.Http.Json;
using System.Text.Json;
using client.DTO;
using client.DTO.Purchase;
using client.Pages;

namespace client.Services.Implementations;

public class PurchaseService : IPurchaseService
{
    private readonly HttpClient client;

    public PurchaseService(HttpClient client)
    {
        this.client = client;
    }

    public async Task<PurchaseResponseDto> CreatePurchaseAsync(PurchaseRequestDto purchaseRequestDto)
    {
        // Send the request to the server to create a Stripe checkout session
        HttpResponseMessage response =
            await client.PostAsJsonAsync($"purchases",
                purchaseRequestDto); 
        String responseContent = await response.Content.ReadAsStringAsync();

        if (response.IsSuccessStatusCode)
        {
            PurchaseResponseDto purchaseResponse =
                JsonSerializer.Deserialize<PurchaseResponseDto>(responseContent,
                    new JsonSerializerOptions
                    {
                        PropertyNameCaseInsensitive = true
                    })!;
            return purchaseResponse;
        }

        throw new Exception(responseContent);

    }
}
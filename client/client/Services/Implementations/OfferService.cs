﻿using System.Globalization;
using System.Net.Http.Json;
using System.Text;
using System.Text.Json;
using client.DTO;
using client.DTO.Offer;
using client.Pages;
using Microsoft.AspNetCore.Components.Sections;
using JsonSerializer = System.Text.Json.JsonSerializer;

namespace client.Services.Implementations;

public class OfferService : IOfferService {
    private readonly HttpClient client;

    public OfferService(HttpClient client) {
        this.client = client;
    }

    public async Task<OfferResponseDto> CreateOfferAsync(
        CreateOfferRequestDto createOfferRequestDto)
    {
        string jsonRequest = JsonSerializer.Serialize(createOfferRequestDto);
        Console.WriteLine(jsonRequest);

        HttpResponseMessage response =
            await client.PostAsJsonAsync($"offers", createOfferRequestDto);
        String responseContent = await response.Content.ReadAsStringAsync();
        Console.WriteLine(responseContent);
        if (response.IsSuccessStatusCode) {
            OfferResponseDto offerResponse =
                JsonSerializer.Deserialize<OfferResponseDto>(responseContent,
                    new JsonSerializerOptions {
                        PropertyNameCaseInsensitive = true
                    })!;
            return offerResponse;
        }

        throw new Exception(responseContent);
    }

    public async Task<List<CategoryDto>> GetCategoriesAsync()
    {
        HttpResponseMessage response = await client.GetAsync("offers/categories");
        string responseContent = await response.Content.ReadAsStringAsync();
        if (response.IsSuccessStatusCode)
        {
            List<CategoryDto> categories = JsonSerializer.Deserialize<List<CategoryDto>>(
                responseContent,
                new JsonSerializerOptions
                {
                    PropertyNameCaseInsensitive = true
                })!;
            return categories;
        }

        throw new Exception(responseContent); 
    }

    public async Task<OfferResponseDto> GetOfferByIdAsync(string id) {
        HttpResponseMessage response =
            await client.GetAsync($"offers/{id}");
        String responseContent = await response.Content.ReadAsStringAsync();
        Console.WriteLine(responseContent);
        if (response.IsSuccessStatusCode) {
            OfferResponseDto offerResponse =
                JsonSerializer.Deserialize<OfferResponseDto>(responseContent,
                    new JsonSerializerOptions {
                        PropertyNameCaseInsensitive = true
                    })!;
            return offerResponse;
        }

        Console.WriteLine(responseContent);
        throw new Exception(responseContent);
    }
 public async Task<List<OfferResponseDto>> GetOffersAsync(FilterRequestDto? filterRequestDto)
{
    
    var queryParameters = new List<string>();

    if (filterRequestDto != null)
    {
        // Add query parameters only if they have values
        if (filterRequestDto.MinOfferPrice.HasValue)
            queryParameters.Add($"minOfferPrice={filterRequestDto.MinOfferPrice}");

        if (filterRequestDto.MaxOfferPrice.HasValue)
            queryParameters.Add($"maxOfferPrice={filterRequestDto.MaxOfferPrice}");

        if (filterRequestDto.PickupTimeStart.HasValue)
            queryParameters.Add($"pickupTimeStart={filterRequestDto.PickupTimeStart.Value.ToString("yyyy-MM-ddTHH:mm:ss")}"); // ISO 8601 format for DateTime

        if (filterRequestDto.PickupTimeEnd.HasValue)
            queryParameters.Add($"pickupTimeEnd={filterRequestDto.PickupTimeEnd.Value.ToString("yyyy-MM-ddTHH:mm:ss")}");

        if (filterRequestDto.Categories != null && filterRequestDto.Categories.Any())
        {
            queryParameters.AddRange(filterRequestDto.Categories.Select(category => $"categories={category}"));
        }

        // Console.WriteLine("Location: " + filterRequestDto.Location.ToString()); // Debugging purposes
        // Add location parameters if present
        if (filterRequestDto.Location != null)
        {
            if (filterRequestDto.Location.Latitude.HasValue)
                queryParameters.Add($"latitude={filterRequestDto.Location.Latitude.Value.ToString(CultureInfo.InvariantCulture)}");

            if (filterRequestDto.Location.Longitude.HasValue)
                queryParameters.Add($"longitude={filterRequestDto.Location.Longitude.Value.ToString(CultureInfo.InvariantCulture)}");

            if (filterRequestDto.Location.Radius.HasValue)
                queryParameters.Add($"radius={filterRequestDto.Location.Radius.Value.ToString(CultureInfo.InvariantCulture)}");
        }
    }

    // Combine query parameters with '&'
    var queryString = queryParameters.Any() ? "?" + string.Join("&", queryParameters) : string.Empty;

    Console.WriteLine(queryString); // Debugging purposes
    var response = await client.GetAsync($"offers{queryString}");
    var json = await response.Content.ReadAsStringAsync();
    var offers = JsonSerializer.Deserialize<List<OfferResponseDto>>(
        json,
        new JsonSerializerOptions
        {
            PropertyNameCaseInsensitive = true
        })!;

    return offers;
}
}
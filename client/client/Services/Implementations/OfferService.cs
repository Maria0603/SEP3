using System.Net.Http.Json;
using System.Text;
using System.Text.Json;
using client.DTO;
using client.Pages;
using Microsoft.AspNetCore.Components.Sections;
using JsonSerializer = System.Text.Json.JsonSerializer;

namespace client.Services.Implementations;

public class OfferService : IOfferService
{
    private readonly HttpClient client;

    public OfferService(HttpClient client)
    {
        this.client = client;
    }

    public async Task<OfferResponseDto> SaveOfferAsync(
        CreateOfferRequestDto createOfferRequestDto)
    {
        string jsonRequest = JsonSerializer.Serialize(createOfferRequestDto);
        Console.WriteLine(jsonRequest);

        HttpResponseMessage response =
            await client.PostAsJsonAsync($"offers", createOfferRequestDto);
        String responseContent = await response.Content.ReadAsStringAsync();
        Console.WriteLine(responseContent);
        if (response.IsSuccessStatusCode)
        {
            OfferResponseDto offerResponse =
                JsonSerializer.Deserialize<OfferResponseDto>(responseContent,
                    new JsonSerializerOptions
                    {
                        PropertyNameCaseInsensitive = true
                    })!;
            return offerResponse;
        }

        throw new Exception(responseContent);
    }

    public async Task<OfferResponseDto> GetOfferByIdAsync(string id)
    {
        HttpResponseMessage response =
            await client.GetAsync($"offers/{id}");
        String responseContent = await response.Content.ReadAsStringAsync();
        Console.WriteLine(responseContent);
        if (response.IsSuccessStatusCode)
        {
            OfferResponseDto offerResponse =
                JsonSerializer.Deserialize<OfferResponseDto>(responseContent,
                    new JsonSerializerOptions
                    {
                        PropertyNameCaseInsensitive = true
                    })!;
            return offerResponse;
        }

        Console.WriteLine(responseContent);
        throw new Exception(responseContent);
    }

    //  TODO: CLEANUP ˇˇˇ bellow ˇˇˇ
    public async Task<List<ShortOfferResponseDto>> GetOffersAsync()
    {
        
        // Log the request URL
        var requestUrl = "offers";
        Console.WriteLine("Request URL: " + client.BaseAddress + requestUrl);

        // Log the headers
        Console.WriteLine("Request Headers:");
        foreach (var header in client.DefaultRequestHeaders)
        {
            Console.WriteLine($"{header.Key}: {string.Join(", ", header.Value)}");
        }

        var response = await client.GetAsync("offers");
        
        var json = await response.Content.ReadAsStringAsync();
        Console.WriteLine("Offer: "+json);
        var offers = JsonSerializer.Deserialize<List<ShortOfferResponseDto>>(
            json,
            new JsonSerializerOptions
            {
                PropertyNameCaseInsensitive = true
            })!;

        return offers;
    }

}
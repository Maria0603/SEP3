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

    public async Task<OfferResponseDto> SaveOfferAsync(
        CreateOfferRequestDto createOfferRequestDto) {
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

    //  TODO: CLEANUP ˇˇˇ bellow ˇˇˇ
    public async Task<List<ShortOfferResponseDto>> GetOffersAsync(FilterRequestDto? filterRequestDto)
{
    var query = new StringBuilder();

    if (filterRequestDto != null)
    {
        query.Append($"?minOfferPrice={filterRequestDto.MinOfferPrice}");
        query.Append($"&maxOfferPrice={filterRequestDto.MaxOfferPrice}");
        query.Append($"&pickupTimeStart={filterRequestDto.PickupTimeStart}");
        query.Append($"&pickupTimeEnd={filterRequestDto.PickupTimeEnd}");

        foreach (var category in filterRequestDto.Categories)
        {
            query.Append($"&categories={category}");
        }
    }

    var response = await client.GetAsync($"offers/filter{query}");
    var json = await response.Content.ReadAsStringAsync();
    var offers = JsonSerializer.Deserialize<List<ShortOfferResponseDto>>(
        json,
        new JsonSerializerOptions
        {
            PropertyNameCaseInsensitive = true
        })!;

    return offers;
}
}
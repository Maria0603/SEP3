using System.Net.Http.Json;
using System.Text;
using System.Text.Json;
using client.DTO;
using client.Pages;
using Newtonsoft.Json;
using JsonSerializer = System.Text.Json.JsonSerializer;

namespace client.Services;

public class OfferService : IOfferService
{
    private readonly HttpClient client;

    public OfferService(HttpClient client)
    {
        this.client = client;
    }

    public async Task<OrderResponseDto> PlaceOrderAsync(string offerId,
        OrderRequestDto orderRequestDto)
    {
        // Send the request to the server to create a Stripe checkout session
        HttpResponseMessage response =
            await client.PostAsJsonAsync($"offers/order",
                orderRequestDto); //TODO: replace with the right endpoint
        String responseContent = await response.Content.ReadAsStringAsync();

        if (response.IsSuccessStatusCode)
        {
            OrderResponseDto orderResponse =
                JsonSerializer.Deserialize<OrderResponseDto>(responseContent,
                    new JsonSerializerOptions
                    {
                        PropertyNameCaseInsensitive = true
                    })!;
            return orderResponse;
        }

        throw new Exception(
            $"Error: {response.StatusCode}, {response.ReasonPhrase}");
    }

    public async Task<OfferResponseDto> SaveOfferAsync(CreateOfferRequestDto createOfferRequestDto)
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

        throw new Exception(
            $"Error: {response.StatusCode}, {response.ReasonPhrase}");
    }

    //  TODO: CLEANUP ˇˇˇ bellow ˇˇˇ
    public async Task<List<Offer>> GetOffersAsync()
    {
        var response = await client.GetAsync("offers");
        var json = await response.Content.ReadAsStringAsync();
        var offers = JsonSerializer.Deserialize<List<Offer>>(json);
        return offers;
    }
    
    public async Task<string> GetOffersJsonAsync()
    {
        var response = await client.GetAsync("offers");
        var json = await response.Content.ReadAsStringAsync();
        return json;
    }
}
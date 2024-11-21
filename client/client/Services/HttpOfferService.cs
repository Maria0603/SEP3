using System.Net.Http.Json;
using System.Text.Json;
using client.Pages;

namespace client.Services;

public class HttpOfferService : IOfferService
{
    private readonly HttpClient client;
    
    public HttpOfferService(HttpClient client)
    {
        this.client = client;
    }
    
    public async Task<List<Offer>>? GetOffersAsync()
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
        var offers = JsonSerializer.Deserialize<List<Offer>>(json);
        return json;
    }
}
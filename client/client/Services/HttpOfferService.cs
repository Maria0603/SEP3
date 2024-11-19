using System.Net.Http.Json;
using client.Pages;

namespace client.Services;

public class HttpOfferService : IOfferService
{
    private readonly HttpClient client;
    
    public HttpOfferService(HttpClient client)
    {
        this.client = client;
    }
    
    public async Task<string> GetOffersAsync()
    {
        var response = await client.GetAsync("offers");
        var json = await response.Content.ReadAsStringAsync();
        return json;
        
        // var offers = await client.GetFromJsonAsync<List<Offer>>("offers");
        // return offers ?? new List<Offer>();
        // return new List<Offer>();

        // return null;
    }
}
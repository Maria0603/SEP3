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
    
    public async Task<List<Offer>> GetOffersAsync()
    {
        // var offers = await client.GetFromJsonAsync<List<Offer>>("api/offers");
        // Testing purposign url
        var offers = await client.GetFromJsonAsync<List<Offer>>("c76787b7-9ad7-461e-a7f0-7881b5da4f17");
        return offers ?? new List<Offer>();
    }
}
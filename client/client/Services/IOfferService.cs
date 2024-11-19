using client.Pages;

namespace client.Services;

public interface IOfferService
{
    public Task<string> GetOffersAsync();
}
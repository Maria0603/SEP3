using client.Pages;

namespace client.Services;

public interface IOfferService
{
    public Task<OrderResponseDto> PlaceOrderAsync(string offerId, OrderRequestDto orderRequestDto);
    
}
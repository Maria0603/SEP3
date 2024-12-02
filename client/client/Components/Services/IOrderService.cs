using client.Pages;
using client.DTO;

namespace client.Services;

public interface IOrderService
{
    public Task<OrderResponseDto> PlaceOrderAsync(OrderRequestDto orderRequestDto);
}
using client.DTO;
using client.Pages;

namespace client.Services;

public interface IOrderService
{
    public Task<OrderResponseDto> PlaceOrderAsync(OrderRequestDto orderRequestDto);
}
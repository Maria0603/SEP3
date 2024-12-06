using client.DTO;
using client.DTO.Order;
using client.Pages;

namespace client.Services;

public interface IOrderService
{
    public Task<OrderResponseDto> PlaceOrderAsync(OrderRequestDto orderRequestDto);
}
using System.Net.Http.Json;
using System.Text.Json;
using client.DTO;
using client.Pages;

namespace client.Services.Implementations;

public class OrderService : IOrderService
{
    private readonly HttpClient client;

    public OrderService(HttpClient client)
    {
        this.client = client;
    }

    public async Task<OrderResponseDto> PlaceOrderAsync(OrderRequestDto orderRequestDto)
    {
        // Send the request to the server to create a Stripe checkout session
        HttpResponseMessage response =
            await client.PostAsJsonAsync($"orders",
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

        throw new Exception(responseContent);

    }
}
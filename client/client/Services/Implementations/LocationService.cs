using System.Text.Json;
using client.DTO.Customer;

namespace client.Services.Implementations;

public class LocationService :ILocationService
{
    private readonly HttpClient client;

    public LocationService(HttpClient client) {
        this.client = client;
    }
    public async Task<CustomerLocationRequestResponseDto> GetCustomerLocationAsync()
    {
        HttpResponseMessage response =
            await client.GetAsync("customers/location");
        String responseContent = await response.Content.ReadAsStringAsync();
        Console.WriteLine(responseContent);
        if (response.IsSuccessStatusCode) {
            CustomerLocationRequestResponseDto location = JsonSerializer.Deserialize<CustomerLocationRequestResponseDto>(responseContent,
                    new JsonSerializerOptions {
                        PropertyNameCaseInsensitive = true
                    })!;
            return location;
        }

        Console.WriteLine(responseContent);
        throw new Exception(responseContent);
    }
    
}
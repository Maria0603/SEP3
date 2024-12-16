using System.Net.Http;
using System.Net.Http.Json;
using System.Text.Json;
using client.DTO.Customer;

namespace client.Services.Implementations;

public class CustomerService : ICustomerService
{
    private readonly HttpClient _client;

    public CustomerService(HttpClient client)
    {
        _client = client;
    }

    public async Task<CustomerResponseDto> GetCustomerByIdAsync(string id)
    {
        try
        {
            HttpResponseMessage response = await _client.GetAsync($"customers/{id}");
            if (response.IsSuccessStatusCode)
            {
                string responseContent = await response.Content.ReadAsStringAsync();
                Console.WriteLine(responseContent);
                return JsonSerializer.Deserialize<CustomerResponseDto>(
                    responseContent,
                    new JsonSerializerOptions { PropertyNameCaseInsensitive = true }
                )!;
            }
            else
            {
                throw new Exception($"API call failed with status code : {await response.Content.ReadAsStringAsync()}");
            }
        }
        catch (Exception ex)
        {
            throw new Exception($"Error calling API: {ex.Message}");
        }
    }

    public async Task<CustomerResponseDto> UpdateCustomerProfileAsync(CustomerUpdateRequestDto updatedProfile)
    {
        try
        {
            HttpResponseMessage response = await _client.PutAsJsonAsync("customers", updatedProfile);
            if (response.IsSuccessStatusCode)
            {
                string responseContent = await response.Content.ReadAsStringAsync();
                Console.WriteLine(responseContent);
                return JsonSerializer.Deserialize<CustomerResponseDto>(
                    responseContent,
                    new JsonSerializerOptions { PropertyNameCaseInsensitive = true }
                )!;
            }
            else
            {
                throw new Exception($": {await response.Content.ReadAsStringAsync()}");
            }
        }
        catch (Exception ex)
        {
            throw new Exception($"{ex.Message}");
        }
    }
}

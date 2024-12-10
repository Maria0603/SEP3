using System.Net.Http.Json;
using client.DTO.Business;

using JsonSerializer = System.Text.Json.JsonSerializer;
using System.Text.Json;
namespace client.Services.Implementations;

public class BusinessService : IBusinessService
{
    private readonly HttpClient client;

    public BusinessService(HttpClient client)
    {
        this.client = client;
    }
    
    public async Task<BusinessResponseDto> GetBusinessByIdAsync(string id)
    {
        try
        {
            HttpResponseMessage response = await client.GetAsync($"businesses/{id}");
            if (response.IsSuccessStatusCode)
            {
                string responseContent = await response.Content.ReadAsStringAsync();
                Console.WriteLine(responseContent);
                return JsonSerializer.Deserialize<BusinessResponseDto>(
                    responseContent,
                    new JsonSerializerOptions { PropertyNameCaseInsensitive = true }
                )!;
            }
            else
            {
                throw new Exception($"API call failed with status code {response.StatusCode}: {await response.Content.ReadAsStringAsync()}");
            }
        }
        catch (Exception ex)
        {
            throw new Exception($"Error calling API: {ex.Message}");
        }
    }
    
    public async Task<BusinessResponseDto> UpdateBusinessProfileAsync(BusinessUpdateRequestDto updatedProfile)
    {
        try
        {
            HttpResponseMessage response = await client.PutAsJsonAsync($"businesses", updatedProfile);
            if (response.IsSuccessStatusCode)
            {
                string responseContent = await response.Content.ReadAsStringAsync();
                Console.WriteLine(responseContent);
                return JsonSerializer.Deserialize<BusinessResponseDto>(
                    responseContent,
                    new JsonSerializerOptions { PropertyNameCaseInsensitive = true }
                )!;
            }
            else
            {
                throw new Exception($"API call failed with status code {response.StatusCode}: {await response.Content.ReadAsStringAsync()}");
            }
        }
        catch (Exception ex)
        {
            throw new Exception($"Error calling API: {ex.Message}");
        }
    }
}
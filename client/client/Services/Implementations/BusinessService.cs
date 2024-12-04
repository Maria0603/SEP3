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
    
    public async Task<BusinessResponseDto> GetBusinessAsync(string id)
    {
        HttpResponseMessage response = 
            await client.GetAsync($"business/{id}");
       String responseContent = await response.Content.ReadAsStringAsync(); 
       Console.WriteLine(responseContent);
       if (response.IsSuccessStatusCode)
       {
           BusinessResponseDto businessResponse =
               JsonSerializer.Deserialize<BusinessResponseDto>(responseContent,
                   new JsonSerializerOptions
                   {
                       PropertyNameCaseInsensitive = true
                   })!;
           return businessResponse;
       }
       
       Console.WriteLine(responseContent);
       throw new Exception(responseContent);
    }
}
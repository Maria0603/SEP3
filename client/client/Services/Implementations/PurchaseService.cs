using System.Net.Http.Json;
using System.Text.Json;
using client.Dto.Purchase;

namespace client.Services.Implementations;

public class PurchaseService : IPurchaseService {
    private readonly HttpClient client;

    public PurchaseService(HttpClient client) {
        this.client = client;
    }

    public async Task<PurchaseResponseDto> CreatePurchaseAsync(
        PurchaseRequestDto purchaseRequestDto) {
        // Send the request to the server to create a Stripe checkout session
        HttpResponseMessage response =
            await client.PostAsJsonAsync($"purchases",
                purchaseRequestDto);
        String responseContent = await response.Content.ReadAsStringAsync();

        if (response.IsSuccessStatusCode) {
            PurchaseResponseDto purchaseResponse =
                JsonSerializer.Deserialize<PurchaseResponseDto>(responseContent,
                    new JsonSerializerOptions {
                        PropertyNameCaseInsensitive = true
                    })!;
            return purchaseResponse;
        }
        throw new Exception(responseContent);
    }

    public async Task<IReadOnlyCollection<PurchaseResponseDto>> GetPurchasesAsync() {
        var response = await client.GetAsync("purchases");
        var json = await response.Content.ReadAsStringAsync();
        var purchases = JsonSerializer.Deserialize<List<PurchaseResponseDto>>(
            json,
            new JsonSerializerOptions { PropertyNameCaseInsensitive = true });

        return purchases;

    }
    
    public async Task<DetailedPurchaseResponseDto> GetDetailedPurchaseByIdAsync(string id)
    {
        HttpResponseMessage response = 
            await client.GetAsync($"purchases/detailed/{id}");
        string responseContent = await response.Content.ReadAsStringAsync();
        Console.WriteLine(responseContent);
        if (response.IsSuccessStatusCode)
        {
            DetailedPurchaseResponseDto purchaseResponseDto =
                JsonSerializer.Deserialize<DetailedPurchaseResponseDto>(responseContent,
                    new JsonSerializerOptions
                    {
                        PropertyNameCaseInsensitive = true
                    })!;
            return purchaseResponseDto;
        }
        throw new Exception(responseContent);
    }
}
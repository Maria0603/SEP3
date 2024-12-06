using System.Net.Http.Headers;
using System.Net.Http.Json;
using System.Text.Json;
using client.DTO.Auth;

namespace client.Services.Implementations;

public class AuthService : IAuthService
{
    private readonly HttpClient client;

    public AuthService(HttpClient client)
    {
        this.client = client;
    }

    public async Task<CredentialsResponseDto> LoginAsync(LoginRequestDto loginRequestDto)
    {
        string jsonRequest = JsonSerializer.Serialize(loginRequestDto);
        Console.WriteLine(jsonRequest);

        HttpResponseMessage response = await client.PostAsJsonAsync("auth/login", loginRequestDto);
        
        if (response.IsSuccessStatusCode)
        {
            String responseContent = await response.Content.ReadAsStringAsync();
            Console.WriteLine(responseContent);
            CredentialsResponseDto tokens = JsonSerializer.Deserialize<CredentialsResponseDto>(responseContent,
                    new JsonSerializerOptions
                    {
                        PropertyNameCaseInsensitive = true
                    })!;
            return tokens;
        }
        
        throw new Exception("Error while logging in.");
    }
    
    public async Task<CredentialsResponseDto> RegisterBusinessAsync (RegisterBusinessRequestDto request)
    {
        HttpResponseMessage response = await client.PostAsJsonAsync("auth/registration/business", request);
        
        if (response.IsSuccessStatusCode)
        {
            String responseContent = await response.Content.ReadAsStringAsync();
            Console.WriteLine(responseContent);
            CredentialsResponseDto tokens = JsonSerializer.Deserialize<CredentialsResponseDto>(responseContent,
                new JsonSerializerOptions
                {
                    PropertyNameCaseInsensitive = true
                })!;
            
            return tokens;
        }
        
        throw new Exception("Error while registering in.");
    }
    public async Task<CredentialsResponseDto> RegisterCustomerAsync (RegisterCustomerRequestDto request)
    {
        HttpResponseMessage response = await client.PostAsJsonAsync("auth/registration/customer", request);
        
        if (response.IsSuccessStatusCode)
        {
            String responseContent = await response.Content.ReadAsStringAsync();
            Console.WriteLine(responseContent);
            CredentialsResponseDto tokens = JsonSerializer.Deserialize<CredentialsResponseDto>(responseContent,
                new JsonSerializerOptions
                {
                    PropertyNameCaseInsensitive = true
                })!;
            
            return tokens;
        }
        
        throw new Exception("Error while registering in.");
    }
    
    public void SetAuthorizationHeader(string? token)
    {
        client.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", token);
    }
    
}
using System;
//using System.IdentityModel.Tokens.Jwt;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Net.Http.Json;
using System.Security.Claims;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;
using Blazored.LocalStorage;
using client.DTO;
using client.DTO.Auth;
using client.Services;
using Microsoft.AspNetCore.Components.Authorization;
using Microsoft.AspNetCore.WebUtilities;

namespace client.Services;

public class AuthService : IAuthService
{
    private readonly HttpClient client;
    public string Token { get; private set; } = string.Empty;
    public string RefreshToken { get; private set; } = string.Empty;

    public AuthService(HttpClient client)
    {
        this.client = client;
    }

    public async Task<bool> LoginAsync(LoginRequestDto loginRequestDto)
    {
        string jsonRequest = JsonSerializer.Serialize(loginRequestDto);
        Console.WriteLine(jsonRequest);

        HttpResponseMessage response = await client.PostAsJsonAsync("auth/login", jsonRequest);
        
        if (response.IsSuccessStatusCode)
        {
            String responseContent = await response.Content.ReadAsStringAsync();
            Console.WriteLine(responseContent);
            CredentialsResponseDto tokens = JsonSerializer.Deserialize<CredentialsResponseDto>(responseContent,
                    new JsonSerializerOptions
                    {
                        PropertyNameCaseInsensitive = true
                    })!;
            Token = tokens.Token;
            RefreshToken = tokens.RefreshToken;
            client.DefaultRequestHeaders.Authorization =
                new AuthenticationHeaderValue("Bearer", Token);
            return true;
        }
        
        return false;
    }
    
    public async Task<bool> RegisterBusinessAsync (RegisterBusinessRequestDto request)
    {
        string jsonRequest = JsonSerializer.Serialize(request);
        Console.WriteLine(jsonRequest);

        HttpResponseMessage response = await client.PostAsJsonAsync("auth/registration/business", jsonRequest);
        
        if (response.IsSuccessStatusCode)
        {
            String responseContent = await response.Content.ReadAsStringAsync();
            Console.WriteLine(responseContent);
            CredentialsResponseDto tokens = JsonSerializer.Deserialize<CredentialsResponseDto>(responseContent,
                new JsonSerializerOptions
                {
                    PropertyNameCaseInsensitive = true
                })!;
            Token = tokens.Token;
            RefreshToken = tokens.RefreshToken;
            client.DefaultRequestHeaders.Authorization =
                new AuthenticationHeaderValue("Bearer", Token);
            return true;
        }
        
        return false;
    }

    public void Logout()
    {
        Token = string.Empty;
        RefreshToken = string.Empty;
        client.DefaultRequestHeaders.Authorization = null;
    }
}
using System;
//using System.IdentityModel.Tokens.Jwt;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Net.Http.Json;
using System.Security.Claims;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;
// using Blazored.LocalStorage;
using client.DTO;
using client.DTO.Auth;
using client.Services;
using Microsoft.AspNetCore.Components.Authorization;
using Microsoft.AspNetCore.WebUtilities;

namespace client.Services;

// public class AuthService : AuthenticationStateProvider, IAuthService
// {
    
//     private readonly HttpClient _httpClient;
//     private readonly ILocalStorageService _localStorage;
//     private ClaimsPrincipal _currentClaimsPrincipal;
//     private const string TokenKey = "authToken";

//     private const string jwttoken =
//         "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY3NGEwYWNjMDU3MWRjMWE3YjQxMDU0MCIsInJvbGUiOiJCdXNpbmVzcyIsImlhdCI6MTUxNjIzOTAyMn0.8Mt-vGnk93oyITTGBwLLd49MJpMMYK0ojMy-gUyjV14";

//     // Constructor accepts ILocalStorageService for localStorage access
//     public AuthService(HttpClient httpClient, ILocalStorageService localStorage)
//     {
//         _httpClient = httpClient;
//         _localStorage = localStorage;
//         AuthenticateAsync(jwttoken);
//     }
    
   
    
//     private async Task AuthenticateAsync(string token)
//     {
//         await _localStorage.SetItemAsync("authToken", token);
//         _currentClaimsPrincipal = GetClaimsFromJwt(token);
//         AuthenticationState authState = new AuthenticationState(_currentClaimsPrincipal);
//         Console.WriteLine("Auth state" + authState.User.Identity.Name);
//         NotifyAuthenticationStateChanged(Task.FromResult(authState));
       
//     }

//     private ClaimsPrincipal GetClaimsFromJwt(string token)
//     {
//         Console.WriteLine("Token: " + token);
//         var handler = new JwtSecurityTokenHandler();

//         var jwtToken = handler.ReadJwtToken(token);

//         var claims = jwtToken.Claims.ToList();

//         // Map the 'name' claim to ClaimTypes.Name
//         var nameClaim = claims.FirstOrDefault(c => c.Type == "id");
//         if (nameClaim != null)
//         {
//             claims.Remove(nameClaim); // Remove the old 'name' claim
//             claims.Add(new Claim(ClaimTypes.Name, nameClaim.Value)); // Add it as ClaimTypes.Name
//         }
        
//         var roleClaim = claims.FirstOrDefault(c => c.Type == "role");
//         if (roleClaim != null)
//         {
//             claims.Remove(roleClaim); // Remove the old 'role' claim
//             claims.Add(new Claim(ClaimTypes.Role, roleClaim.Value)); // Add it as ClaimTypes.Role
//         }

//         foreach (var claim in claims)
//         {
//             Console.WriteLine($"{claim.Type}: {claim.Value}");
//         }

//         var identity = new ClaimsIdentity(claims, "jwt");
//         return new ClaimsPrincipal(identity);
//     }



    
//     public async Task<CredentialsBusinessResponseDto> LoginBusiness(LoginBusinessRequestDto loginBusinessRequestDto)
//     {
//         var response = await _httpClient.PostAsJsonAsync("auth/login/business", loginBusinessRequestDto);

//         if (!response.IsSuccessStatusCode)
//         {
//             throw new Exception(response.ReasonPhrase);
//         }
//         var credentialsBusiness = await response.Content.ReadFromJsonAsync<CredentialsBusinessResponseDto>();
//         if (credentialsBusiness != null)
//         {
//             AuthenticateAsync(credentialsBusiness.Token);
//         }
//         return credentialsBusiness;
//     }

//     // Save the JWT token to localStorage
//     public void SaveToken(string token)
//     {
//         // _localStorage.SetItemAsync(TokenKey, token);  // using JSInterop or Blazored.LocalStorage
//         //
//         // _authStateProvider.MarkUserAsAuthenticated(token);
//     }

//     // Get the JWT token from localStorage
//     public async Task<string> GetTokenAsync()
//     {
//         return await _localStorage.GetItemAsync<string>(TokenKey); // Using JSInterop or Blazored.LocalStorage
//     }

//     // Check if the user is authenticated
//     public async Task<bool> IsAuthenticated()
//     {
//         var token = await GetTokenAsync();
//         return !string.IsNullOrEmpty(token); // Simple check for the existence of the token
//     }

//     // Logout method to clear the JWT token from localStorage
//     public async Task Logout()
//     {
//         // await _localStorage.RemoveItemAsync(TokenKey);
//         _currentClaimsPrincipal = new(); 
//         NotifyAuthenticationStateChanged(Task.FromResult(new AuthenticationState(_currentClaimsPrincipal)));

//         // _authStateProvider.MarkUserAsLoggedOut();
//     }

//     // Set Authorization Header with the token for API calls
//     public void SetAuthHeader(HttpClient client)
//     {
//         var token = _localStorage.GetItemAsync<string>(TokenKey).Result;
//         if (!string.IsNullOrEmpty(token))
//         {
//             client.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", token);
//         }
//     }

//     public override async Task<AuthenticationState> GetAuthenticationStateAsync()
//     {
//         // Return the current authentication state with the currentClaimsPrincipal
//         return new AuthenticationState(_currentClaimsPrincipal ?? new ());
//     }
// }

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

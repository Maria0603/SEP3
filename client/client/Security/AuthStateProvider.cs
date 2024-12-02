using System.Net.Http.Json;
using System.Security.Claims;
using System.Text.Json;
using System.Transactions;
using client.Services;
using Microsoft.AspNetCore.Components.Authorization;
using Microsoft.AspNetCore.Components.Sections;
using Microsoft.JSInterop;

namespace client.Security;

public class AuthStateProvider : AuthenticationStateProvider
{
    private readonly AuthService _authService;

    public AuthStateProvider(AuthService authService)
    {
        _authService = authService;
    }

    public override Task<AuthenticationState> GetAuthenticationStateAsync()
    {
        // Check if the user is authenticated based on the presence of a token
        if (string.IsNullOrEmpty(_authService.Token))
        {
            // User is not authenticated
            var anonymous = new ClaimsPrincipal(new ClaimsIdentity());
            return Task.FromResult(new AuthenticationState(anonymous));
        }

        // Parse claims from the token (assuming JWT, implement parsing logic here)
        var identity = new ClaimsIdentity(ParseClaimsFromJwt(_authService.Token), "Bearer");
        var user = new ClaimsPrincipal(identity);

        return Task.FromResult(new AuthenticationState(user));
    }

    public void NotifyUserAuthentication(string token)
    {
        // Notify the Blazor app about the updated authentication state
        var identity = new ClaimsIdentity(ParseClaimsFromJwt(token), "Bearer");
        var user = new ClaimsPrincipal(identity);
        NotifyAuthenticationStateChanged(Task.FromResult(new AuthenticationState(user)));
    }

    public void NotifyUserLogout()
    {
        // Notify the Blazor app about logout
        var anonymous = new ClaimsPrincipal(new ClaimsIdentity());
        NotifyAuthenticationStateChanged(Task.FromResult(new AuthenticationState(anonymous)));
    }

    private IEnumerable<Claim> ParseClaimsFromJwt(string jwt)
    {
        var claims = new List<Claim>();

        // Assuming the JWT structure (Header.Payload.Signature)
        var payload = jwt.Split('.')[1];
        var jsonBytes = ParseBase64WithoutPadding(payload);
        var keyValuePairs = JsonSerializer.Deserialize<Dictionary<string, object>>(jsonBytes);

        if (keyValuePairs == null)
            return claims;

        foreach (var kvp in keyValuePairs)
        {
            if (kvp.Key == "roles" && kvp.Value is JsonElement { ValueKind: JsonValueKind.Array } roles)
            {
                foreach (var role in roles.EnumerateArray())
                {
                    claims.Add(new Claim(ClaimTypes.Role, role.GetString()!));
                }
            }
            else
            {
                claims.Add(new Claim(kvp.Key, kvp.Value.ToString()!));
            }
        }

        return claims;
    }

    private byte[] ParseBase64WithoutPadding(string base64)
    {
        switch (base64.Length % 4)
        {
            case 2: base64 += "=="; break;
            case 3: base64 += "="; break;
        }

        return Convert.FromBase64String(base64);
    }
}


/*
>>>>>>> Stashed changes
public class AuthStateProvider : AuthenticationStateProvider
{
    private readonly HttpClient httpClient;
    private readonly IJSRuntime jsRuntime;
    private string? cachedUserJson;

    public AuthStateProvider(HttpClient httpClient, IJSRuntime jsRuntime)
    {
        this.httpClient = httpClient;
        this.jsRuntime = jsRuntime;
        cachedUserJson = null;
    }

    public async Task Login(string username, string password)
    {
        Console.WriteLine("Username: " + username + " Password: " + password);
        HttpResponseMessage response = await httpClient.PostAsJsonAsync(
            "auth/login",
            new LoginRequest { Username = username, Password = password });

        string content = await response.Content.ReadAsStringAsync();

        if (!response.IsSuccessStatusCode)
        {
            throw new Exception(content);
        }

        AddUserResponseDto userDto =
            JsonSerializer.Deserialize<AddUserResponseDto>(content,
                new JsonSerializerOptions
                {
                    PropertyNameCaseInsensitive = true
                })!;

        string serialisedData = JsonSerializer.Serialize(userDto);
        await jsRuntime.InvokeVoidAsync("sessionStorage.setItem", "currentUser",
            serialisedData);

        List<Claim> claims = new List<Claim>()
        {
            new Claim(ClaimTypes.Name, userDto.Username),
            new Claim("Id", userDto.UserId.ToString())
            //new Claim("DateOfBirth", userDto.DateOfBirth.ToString("yyyy-MM-dd"));
            // new Claim("IsAdmin", userDto.IsAdmin.ToString())
            // new Claim("IsModerator", userDto.IsModerator.ToString())
            // new Claim("Email", userDto.Email)
        };

        ClaimsIdentity identity = new ClaimsIdentity(claims, "apiauth");
        ClaimsPrincipal claimsPrincipal = new ClaimsPrincipal(identity);

        NotifyAuthenticationStateChanged(
            Task.FromResult(new AuthenticationState(claimsPrincipal)));
    }

    public async void Logout()
    {
        await jsRuntime.InvokeVoidAsync("sessionStorage.setItem", "currentUser",
            "");
        cachedUserJson = null;
        NotifyAuthenticationStateChanged(
            Task.FromResult(new AuthenticationState(new())));
    }

    public override async Task<AuthenticationState>
        GetAuthenticationStateAsync()
    {
        if (string.IsNullOrEmpty(cachedUserJson))
        {
            try
            {
                cachedUserJson =
                    await jsRuntime.InvokeAsync<string>(
                        "sessionStorage.getItem",
                        "currentUser");
            }
            catch (Exception e)
            {
                return new AuthenticationState(new());
            }
        }

        if (string.IsNullOrEmpty(cachedUserJson))
            return new AuthenticationState(new());

        AddUserResponseDto userDto =
            JsonSerializer.Deserialize<AddUserResponseDto>(cachedUserJson)!;

        List<Claim> claims = new List<Claim>()
        {
            new Claim(ClaimTypes.Name, userDto.Username),
            new Claim(ClaimTypes.NameIdentifier, userDto.UserId.ToString())
        };

        ClaimsIdentity identity = new ClaimsIdentity(claims, "apiauth");
        ClaimsPrincipal claimsPrincipal = new ClaimsPrincipal(identity);
        return new AuthenticationState(claimsPrincipal);
    }
}
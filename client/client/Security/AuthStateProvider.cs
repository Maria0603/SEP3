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
    // private readonly AuthService _authService;
    //
    // public AuthStateProvider(AuthService authService)
    // {
    //     _authService = authService;
    // }

    public override async Task<AuthenticationState> GetAuthenticationStateAsync()
    {
        string token = "";
        var identity = new ClaimsIdentity();
        var user = new ClaimsPrincipal(identity);
        var state = new AuthenticationState(user);
        
        NotifyAuthenticationStateChanged(Task.FromResult(state));
        return state;
        // // Check if the user is authenticated based on the presence of a token
        // if (string.IsNullOrEmpty(_authService.Token))
        // {
        //     // User is not authenticated
        //     var anonymous = new ClaimsPrincipal(new ClaimsIdentity());
        //     return Task.FromResult(new AuthenticationState(anonymous));
        // }
        //
        // // Parse claims from the token (assuming JWT, implement parsing logic here)
        // var identity = new ClaimsIdentity(ParseClaimsFromJwt(_authService.Token), "Bearer");
        // var user = new ClaimsPrincipal(identity);
        //
        // return Task.FromResult(new AuthenticationState(user));
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



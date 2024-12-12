using System.Security.Claims;
using System.Text.Json;
using System.Threading.Tasks;
using Blazored.LocalStorage;
using Microsoft.AspNetCore.Components.Authorization;
using client.Services;
using client.DTO.Auth;
using Microsoft.AspNetCore.Components;


namespace client.Security;


public class AuthStateProvider : AuthenticationStateProvider
{
    private readonly IAuthService _authService;
    private readonly ILocalStorageService _localStorage;
    private readonly NavigationManager _navigationManager;
    
    private readonly string _tokenKey="authToken";
    private readonly string _refreshTokenKey="refreshToken";


    public AuthStateProvider(IAuthService authService,
        ILocalStorageService localStorage, NavigationManager navigationManager)
    {
        _authService = authService;
        _localStorage = localStorage;
        _navigationManager = navigationManager;
    }

    public override async Task<AuthenticationState>
        GetAuthenticationStateAsync()
    { 
        // Retrieve token from local storage
        var token = await _localStorage.GetItemAsStringAsync(_tokenKey);
        if (string.IsNullOrEmpty(token))
        {
            var uri = _navigationManager.Uri;
            if (string.IsNullOrEmpty(token) && !uri.Contains("/auth/login"))
                _navigationManager.NavigateTo("/auth/login", true);
            return new AuthenticationState(
                new ClaimsPrincipal(new ClaimsIdentity()));
        }
        

        // Validate token (add a token validation step here if needed, but I don't think so)
        var identity = new ClaimsIdentity(ParseClaimsFromJwt(token), "jwt");
        var user = new ClaimsPrincipal(identity);

        // Set the default authorization header
        _authService.SetAuthorizationHeader(token);

        return new AuthenticationState(user);
    }

    public async Task LoginAsync(LoginRequestDto loginRequest)
    {
        CredentialsResponseDto? tokens = await _authService.LoginAsync(loginRequest);

        if (tokens != null)
        {
            await _localStorage.SetItemAsStringAsync(_tokenKey, tokens.Token);
            await _localStorage.SetItemAsStringAsync(_refreshTokenKey,
                tokens.RefreshToken);

            NotifyAuthenticationStateChanged(GetAuthenticationStateAsync());
        }
    }

    public async Task RegisterBusinessAsync(RegisterBusinessRequestDto registerRequest)
    {
        CredentialsResponseDto? tokens = await _authService.RegisterBusinessAsync(registerRequest);

        if (tokens != null)
        {
            await _localStorage.SetItemAsStringAsync(_tokenKey, tokens.Token);
            await _localStorage.SetItemAsStringAsync(_refreshTokenKey,
                tokens.RefreshToken);

            NotifyAuthenticationStateChanged(GetAuthenticationStateAsync());
        }
    }

    public async Task RegisterCustomerAsync(RegisterCustomerRequestDto registerRequest)
    {
        CredentialsResponseDto? tokens = await _authService.RegisterCustomerAsync(registerRequest);
        
        if (tokens != null)
        {
            await _localStorage.SetItemAsStringAsync(_tokenKey, tokens.Token);
            await _localStorage.SetItemAsStringAsync(_refreshTokenKey,
                tokens.RefreshToken);

            NotifyAuthenticationStateChanged(GetAuthenticationStateAsync());
        }
    }

    public async Task LogoutAsync()
    {
        _authService.SetAuthorizationHeader(null);

        await _localStorage.RemoveItemAsync(_tokenKey);
        await _localStorage.RemoveItemAsync(_refreshTokenKey);

        NotifyAuthenticationStateChanged(Task.FromResult(
            new AuthenticationState(
                new ClaimsPrincipal(new ClaimsIdentity()))));
        _navigationManager.NavigateTo("/auth/login", true);
    }

    private IEnumerable<Claim> ParseClaimsFromJwt(string jwt)
    {
        var claims = new List<Claim>();
        var payload = jwt.Split('.')[1];
        var jsonBytes = ParseBase64WithoutPadding(payload);
        var keyValuePairs =
            JsonSerializer.Deserialize<Dictionary<string, object>>(jsonBytes);

        if (keyValuePairs != null)
        {
            foreach (var kvp in keyValuePairs)
            {
                claims.Add(new Claim(kvp.Key, kvp.Value.ToString()));
            }
        }

        return claims;
    }

    private byte[] ParseBase64WithoutPadding(string base64)
    {
        switch (base64.Length % 4)
        {
            case 2:
                base64 += "==";
                break;
            case 3:
                base64 += "=";
                break;
        }

        return Convert.FromBase64String(base64);
    }
    
    public async Task<string> GetUserRoleAsync()
    {
        var authState = await GetAuthenticationStateAsync();
        var user = authState.User;

        if (user.Identity != null && user.Identity.IsAuthenticated)
        {
            Console.WriteLine("User is authenticated.");
            foreach (var claim in user.Claims)
            {
                Console.WriteLine($"Claim: {claim.Type} = {claim.Value}");
            }

            var roleClaim = user.Claims.FirstOrDefault(c => c.Type == "role")?.Value;
            if (!string.IsNullOrEmpty(roleClaim))
            {
                // Handle role claim as array
                try
                {
                    var roles = JsonSerializer.Deserialize<List<string>>(roleClaim);
                    if (roles != null && roles.Any())
                    {
                        var role = roles.First().ToUpper(); // Use the first role
                        if (role == "BUSINESS" || role == "CUSTOMER")
                        {
                            return role;
                        }
                        Console.WriteLine($"Invalid role found: {role}");
                    }
                    else
                    {
                        Console.WriteLine("Role claim is empty.");
                    }
                }
                catch (Exception ex)
                {
                    Console.WriteLine($"Error parsing role claim: {ex.Message}");
                }
            }
            else
            {
                Console.WriteLine("Role claim is missing.");
            }
        }
        else
        {
            Console.WriteLine("User is not authenticated.");
        }

        throw new InvalidOperationException("User is not authenticated or role is missing.");
    }


}
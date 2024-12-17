using client.Dto.Auth;

namespace client.Services;

public interface IAuthService
{
    public Task<CredentialsResponseDto> LoginAsync(LoginRequestDto request);
    public Task<CredentialsResponseDto> RegisterBusinessAsync (RegisterBusinessRequestDto request);
    public Task<CredentialsResponseDto> RegisterCustomerAsync (RegisterCustomerRequestDto request);
    public void SetAuthorizationHeader(string token);
    
    

}
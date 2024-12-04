using client.DTO.Auth;

namespace client.Services;

public interface IAuthService
{
    public Task<CredentialsResponseDto> LoginAsync(LoginRequestDto request);
    public Task<CredentialsResponseDto> RegisterBusinessAsync (RegisterBusinessRequestDto request);
    public void SetAuthorizationHeader(string token);

}
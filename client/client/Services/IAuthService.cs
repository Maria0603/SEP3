using client.DTO.Auth;

namespace client.Services;

public interface IAuthService
{
    public Task<bool> LoginAsync(LoginRequestDto request);
    public Task<bool> RegisterBusinessAsync (RegisterBusinessRequestDto request);

}
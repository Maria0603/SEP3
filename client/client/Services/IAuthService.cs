using client.DTO;

namespace client.Services;

public interface IAuthService
{
    public Task<CredentialsBusinessResponseDto> LoginBusiness (LoginBusinessRequestDto loginBusinessRequestDto);
}
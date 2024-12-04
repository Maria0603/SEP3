using client.DTO.Business;

namespace client.Services;

public interface IBusinessService
{
    public Task<BusinessResponseDto> GetBusinessAsync(string id);
}
using client.DTO.Business;

namespace client.Services;

public interface IBusinessService
{
    public Task<BusinessResponseDto> GetBusinessByIdAsync(string id);
    public Task<BusinessResponseDto> UpdateBusinessProfileAsync(BusinessUpdateRequestDto request);

}
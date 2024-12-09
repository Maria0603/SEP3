using client.DTO.Customer;

namespace client.Services;

public interface ILocationService
{
    public Task<CustomerLocationRequestResponseDto> GetCustomerLocationAsync();

}
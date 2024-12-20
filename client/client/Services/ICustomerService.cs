using client.Dto.Customer;

namespace client.Services;

public interface ICustomerService
{
    Task<CustomerResponseDto> GetCustomerByIdAsync(string id); 
    Task<CustomerResponseDto> UpdateCustomerProfileAsync(CustomerUpdateRequestDto request);
}
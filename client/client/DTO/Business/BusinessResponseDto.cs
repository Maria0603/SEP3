using Stripe;

namespace client.DTO.Business;

public class BusinessResponseDto
{
    public string Id { get; set; }
    public string Email { get; set; }
    public string PhoneNumber { get; set; }
    public AddressDto Address { get; set; }
    public string Logo_path { get; set; }
    public string Cvr { get; set; }
    public string BusinessName { get; set; }
    public string Role { get; set; }
}
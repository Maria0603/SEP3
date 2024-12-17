namespace client.Dto.Auth;

public class RegisterCustomerRequestDto
{
    public string FirstName { get; set; }
    public string LastName { get; set; }
    public string Email { get; set; }
    public string Password { get; set; }
    public string PhoneNumber { get; set; }
    public AddressDto Address { get; set; }
}
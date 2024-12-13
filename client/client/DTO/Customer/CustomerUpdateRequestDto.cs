namespace client.DTO.Customer;

public class CustomerUpdateRequestDto
{
    public string Id { get; set; }
    public string Email { get; set; }
    public string PhoneNumber { get; set; }
    public string FirstName { get; set; }
    public string LastName { get; set; }
}
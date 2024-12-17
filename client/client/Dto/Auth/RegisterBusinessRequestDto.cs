namespace client.Dto.Auth;

public class RegisterBusinessRequestDto
{
        public string BusinessName { get; set; }
        public string Cvr { get; set; }
        public string Email { get; set; }
        public string Password { get; set; }
        public string PhoneNumber { get; set; }
        public AddressDto Address { get; set; }
        public byte[] Logo { get; set; }
}
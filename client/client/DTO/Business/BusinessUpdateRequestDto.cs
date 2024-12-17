namespace client.DTO.Business;

public class BusinessUpdateRequestDto
{
    public string Id { get; set; }
    public string Email { get; set; }
    public string PhoneNumber { get; set; }
    public string ImageName { get; set; }
    public byte[] Image { get; set; }
    
    
    public string BusinessName { get; set; }
}
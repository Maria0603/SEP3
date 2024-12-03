namespace client.DTO;

public class OrderRequestDto
{
    public string OfferId { get; set; }
    public int NumberOfItems { get; set; }
    public string UserId { get; set; }
}
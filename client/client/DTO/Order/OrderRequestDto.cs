namespace client.DTO.Order;

public class OrderRequestDto
{
    public string OfferId { get; set; }
    public int NumberOfItems { get; set; }
    public string UserId { get; set; }
}
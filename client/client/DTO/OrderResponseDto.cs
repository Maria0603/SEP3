namespace client.Pages;

public class OrderResponseDto
{
    public string OrderId { get; set; }
    public string OfferId { get; set; }
    public string OfferTitle { get; set; }
    public int Quantity { get; set; }
    public DateTime TimeStamp { get; set; }
    public string Url { get; set; }
    // other things from the order, not my job
}
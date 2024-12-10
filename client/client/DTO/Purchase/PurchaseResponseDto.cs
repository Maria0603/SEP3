namespace client.DTO.Purchase;

public class PurchaseResponseDto
{
    public string PurchaseId { get; set; }
    public string CustomerId { get; set; }
    public string OfferId { get; set; }
    public int NumberOfItems { get; set; }
    public string status { get; set; }
    public DateTime purchaseTime { get; set; }
    public int PricePerItem { get; set; }
    public string BusinessName { get; set; }
}
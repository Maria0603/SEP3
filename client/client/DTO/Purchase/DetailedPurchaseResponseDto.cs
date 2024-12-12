namespace client.DTO.Purchase;

public class DetailedPurchaseResponseDto
{
    public int Id { get; set; }
    public string Title { get; set; }
    public string Description { get; set; }
    public int NumberOfPurchasedItems { get; set; }
    public string ImagePath { get; set; }
    public DateTime PickupDate { get; set; }
    public DateTime PickupTimeStart { get; set; }
    public DateTime PickupTimeEnd { get; set; }
    public string CustomerFirstName { get; set; }
    public string CustomerLastName { get; set; }
    public string CustomerEmail { get; set; }
    public string CustomerPhoneNumber { get; set; }
    public DateTime purchaseTime { get; set; }
}
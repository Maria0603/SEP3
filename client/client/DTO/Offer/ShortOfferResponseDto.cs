namespace client.DTO.Offer;

public class ShortOfferResponseDto
{
    public string Id { get; set; }
    public string Title { get; set; }
    public int OriginalPrice { get; set; }
    public int OfferPrice { get; set; }
    public int NumberOfAvailableItems { get; set; }
    public DateTime PickupTimeStart { get; set; }
    public DateTime PickupTimeEnd { get; set; }
    public string ImagePath { get; set; }
    public string Status { get; set; }
}
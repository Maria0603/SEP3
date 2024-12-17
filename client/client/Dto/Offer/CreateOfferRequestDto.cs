namespace client.Dto.Offer;

public class CreateOfferRequestDto
{
    public string Title { get; set; }
    public string Description { get; set; }
    public int OriginalPrice { get; set; }
    public int OfferPrice { get; set; }
    public int NumberOfItems { get; set; }
    public DateTime? PickupTimeStart { get; set; }
    public DateTime? PickupTimeEnd { get; set; }
    public List<string>? Categories { get; set; }
    public byte[]? Image { get; set; }
}
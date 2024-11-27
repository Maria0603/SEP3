namespace client.DTO;

public class ShortOfferResponseDto
{
    public string Id { get; set; }
    public string Title { get; set; }
    public int OriginalPrice { get; set; }
    public int OfferPrice { get; set; }
    public int NumberOfItems { get; set; }
    public DateDto PickupDate { get; set; }
    public TimeDto PickupTimeStart { get; set; }
    public TimeDto PickupTimeEnd { get; set; }
    public string ImagePath { get; set; }
    public string Status { get; set; }
}
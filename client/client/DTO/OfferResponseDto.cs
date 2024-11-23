using client.DTO;

namespace client.Pages;

public class OfferResponseDto
{
    public string Id { get; set; }
    public string Title { get; set; }
    public string Description { get; set; }
    public int OriginalPrice { get; set; }
    public int OfferPrice { get; set; }
    public int NumberOfItems { get; set; }
    public DateDto PickupDate { get; set; }
    public TimeDto PickupTimeStart { get; set; }
    public TimeDto PickupTimeEnd { get; set; }
    public List<string> Categories { get; set; }
    public string ImagePath { get; set; }
    public string Status { get; set; }
}
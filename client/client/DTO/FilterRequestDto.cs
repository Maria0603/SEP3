using client.DTO.Offer;

namespace client.DTO;

public class FilterRequestDto {
    public int MinOfferPrice { get; set; }
    public int MaxOfferPrice { get; set; }
    public DateTime? PickupTimeStart { get; set; }
    public DateTime? PickupTimeEnd { get; set; }
    public List<string>? Categories { get; set; }
    
    public CreateOfferRequestDto ToCreateOfferRequestDto() {
        return new CreateOfferRequestDto {
            PickupTimeStart = PickupTimeStart,
            PickupTimeEnd = PickupTimeEnd,
            Categories = Categories
        };
    }
}
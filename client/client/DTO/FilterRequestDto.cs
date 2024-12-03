namespace client.DTO;

public class FilterRequestDto {
    public int MinOfferPrice { get; set; }
    public int MaxOfferPrice { get; set; }
    public TimeDto PickupTimeStart { get; set; }
    public TimeDto PickupTimeEnd { get; set; }
    public List<string>? Categories { get; set; }
    
    public CreateOfferRequestDto ToCreateOfferRequestDto() {
        return new CreateOfferRequestDto {
            PickupTimeStart = PickupTimeStart,
            PickupTimeEnd = PickupTimeEnd,
            Categories = Categories
        };
    }
}
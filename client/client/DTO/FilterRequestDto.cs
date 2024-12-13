using client.DTO.Offer;

namespace client.DTO;

public class FilterRequestDto {
    public int? MinOfferPrice { get; set; }
    public int? MaxOfferPrice { get; set; }
    public DateTime? PickupTimeStart { get; set; }
    public DateTime? PickupTimeEnd { get; set; }
    public List<string>? Categories { get; set; }
    
    public LocationDto? Location { get; set; }
    
    public String? TextSearch { get; set; }
    
    
   
}
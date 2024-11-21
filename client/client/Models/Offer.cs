
using System.Text.Json.Serialization;

namespace client.Pages;

public class Offer
{
    [JsonPropertyName("id")]
    // Information about the top section
    public string Id { get; set; }
    [JsonPropertyName("numberOfItems")]
    public int NumberOfItems { get; set; } // For the "5 Left" text
    [JsonIgnore]
    public bool IsFavourite { get; set; } // For the heart icon to indicate if the offer is a favorite
    [JsonIgnore]
    public Business BusinessDetails { get; set; } // Business details (name, icon)

    
    // [JsonPropertyName("image")]
    [JsonIgnore]
    public string Image { get; set; } // Image source URL for the offer

    [JsonPropertyName("title")]
    public string Title { get; set; } // Product name (Pastry)
   
    
    
    // [JsonPropertyName("pickupDate")]
    [JsonIgnore]
    public DateTime? PickupTime { get; set; } // Pickup time (Collect tomorrow 21.00 - 22.00)
    
    
    
    
    [JsonIgnore]
    public decimal Rating { get; set; } // Rating value (e.g., 3.5)
    [JsonIgnore]
    public double Distance { get; set; } // Distance (e.g., 4.3 km)
    [JsonPropertyName("originalPrice")]
    public decimal Price { get; set; } // Price of the offer (e.g., 39,00 kr.)
}
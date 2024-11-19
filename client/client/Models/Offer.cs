
using System.Text.Json.Serialization;

namespace client.Pages;

public class Offer
{
    [JsonPropertyName("id")]
    // Information about the top section
    public int Id { get; set; }
    [JsonPropertyName("numberOfItems")]
    public int QuantityLeft { get; set; } // For the "5 Left" text
    [JsonIgnore]
    public bool IsFavourite { get; set; } // For the heart icon to indicate if the offer is a favorite
    [JsonIgnore]
    public Business BusinessDetails { get; set; } // Business details (name, icon)

    // Information about the offer image
    public string OfferImage { get; set; } // Image source URL for the offer

    // Information about the bottom section
    public string ProductName { get; set; } // Product name (Pastry)
    public DateTimeOffset PickupTime { get; set; } // Pickup time (Collect tomorrow 21.00 - 22.00)
    public decimal Rating { get; set; } // Rating value (e.g., 3.5)
    public double Distance { get; set; } // Distance (e.g., 4.3 km)
    public decimal Price { get; set; } // Price of the offer (e.g., 39,00 kr.)
}
using System.Text.Json;
using System.Text.Json.Serialization;

namespace client.Models;

public class Offer
{
    [JsonPropertyName("id")] public string Id { get; set; }
    [JsonPropertyName("numberOfItems")] public int NumberOfItems { get; set; } // For the "5 Left" text

    [JsonPropertyName("imagePath")] public string Image { get; set; } // Image source URL for the offer
    [JsonPropertyName("title")] public string Title { get; set; } // Product name (Pastry)
    [JsonPropertyName("originalPrice")] public int Price { get; set; } // Price of the offer (e.g., 39,00 kr.)
    [JsonPropertyName("pickupDate")] public PickupDate PickupDate { get; set; } = new PickupDate(); // Default to non-null
    [JsonPropertyName("pickupTimeStart")] public PickupTime PickupTimeStart { get; set; } = new PickupTime(0, 0); // Initialize to 00:00
    [JsonPropertyName("pickupTimeEnd")] public PickupTime PickupTimeEnd { get; set; } = new PickupTime(0, 0); // Initialize to 00:00
    
    /*                  UNUSED STUFF                     */
    /* <===============================================> */
    [JsonPropertyName("status")] public string Status { get; set; }
    [JsonIgnore] public bool IsFavourite { get; set; } // For the heart icon to indicate if the offer is a favorite
    //[JsonIgnore] public Business BusinessDetails { get; set; } // Business details (name, icon)
    [JsonIgnore] public decimal Rating { get; set; } // Rating value (e.g., 3.5)
    [JsonIgnore] public double Distance { get; set; } // Distance (e.g., 4.3 km)
    public override string ToString()
    {
        return $"Offer: {Title}\n" +
               $"Number of Items: {NumberOfItems}\n" +
               $"Price: {Price:C2}\n" +  // Price formatted as currency
               $"Pickup Date: {PickupDate.ToDateTime():yyyy-MM-dd}\n" + // Format date to "yyyy-MM-dd"
               $"Pickup Time Start: {PickupTimeStart.Hour:D2}:{PickupTimeStart.Minute:D2}\n" + // Format as HH:mm
               $"Pickup Time End: {PickupTimeEnd.Hour:D2}:{PickupTimeEnd.Minute:D2}";
    }
}

public class PickupTime
{
    public PickupTime(int hour, int minute)
    {
        Hour = hour;
        Minute = minute;
    }

    [JsonPropertyName("hour")] public int Hour { get; set; }
    [JsonPropertyName("minute")] public int Minute { get; set; }

    // Helper method to convert Time to TimeSpan
    public TimeSpan ToTimeSpan() => new TimeSpan(Hour, Minute, 0);
}



public class PickupDate
{
    [JsonPropertyName("year")] public int Year { get; set; }
    [JsonPropertyName("month")] public int Month { get; set; }
    [JsonPropertyName("day")] public int Day { get; set; }
    public DateTime ToDateTime() => new DateTime(Year, Month, Day);
}


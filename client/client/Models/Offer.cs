using System.Text.Json;
using System.Text.Json.Serialization;

namespace client.Pages;

public class Offer
{
    [JsonPropertyName("id")] public string Id { get; set; }
    [JsonPropertyName("numberOfItems")] public int NumberOfItems { get; set; } // For the "5 Left" text

    [JsonPropertyName("imagePath")] public string Image { get; set; } // Image source URL for the offer
    [JsonPropertyName("title")] public string Title { get; set; } // Product name (Pastry)
    [JsonPropertyName("originalPrice")] public decimal Price { get; set; } // Price of the offer (e.g., 39,00 kr.)



    [JsonPropertyName("pickupDate")] public PickupDate PickupDate { get; set; } // Pickup time (Collect tomorrow 21.00 - 22.00)
    [JsonPropertyName("pickupTimeStart")] public Time PickupTimeStart { get; set; }
    [JsonPropertyName("pickupTimeEnd")] public Time PickupTimeEnd { get; set; }


    /*        FOR CREATE FORM COMPUTING          */
    /* <========================================>  */
    [JsonIgnore] public DateTime PickupDateTime
    {
        get => PickupDate.ToDateTime();
        set
        {
            if (PickupDate == null)
            {
                PickupDate = new PickupDate();
            }

            PickupDate.Year = value.Year;
            PickupDate.Month = value.Month;
            PickupDate.Day = value.Day;
        }
    }
    


    /*        UNUSED STUFF          */
    /* <========================================>  */
    [JsonPropertyName("status")] public string Status { get; set; }
    [JsonIgnore] public bool IsFavourite { get; set; } // For the heart icon to indicate if the offer is a favorite
    [JsonIgnore] public Business BusinessDetails { get; set; } // Business details (name, icon)
    [JsonIgnore] public decimal Rating { get; set; } // Rating value (e.g., 3.5)
    [JsonIgnore] public double Distance { get; set; } // Distance (e.g., 4.3 km)
}

public class Time
{
    [JsonPropertyName("hour")] public int Hour { get; set; }
    [JsonPropertyName("minute")] public int Minute { get; set; }

    // Parameterless constructor for deserialization
    public Time() { }

    // Constructor for manual initialization
    public Time(int hour, int minute)
    {
        Hour = hour;
        Minute = minute;
    }

    public TimeSpan ToTimeSpan() => new TimeSpan(Hour, Minute, 0);
}


public class PickupDate
{
    [JsonPropertyName("year")] public int Year { get; set; }

    [JsonPropertyName("month")] public int Month { get; set; }

    [JsonPropertyName("day")] public int Day { get; set; }

    // Helper to convert to DateTime
    public DateTime ToDateTime() => new DateTime(Year, Month, Day);
}

public class TimeJsonConverter : JsonConverter<Time>
{
    public override Time Read(ref Utf8JsonReader reader, Type typeToConvert, JsonSerializerOptions options)
    {
        var json = JsonDocument.ParseValue(ref reader).RootElement;
        var hour = json.GetProperty("hour").GetInt32();
        var minute = json.GetProperty("minute").GetInt32();
        return new Time(hour, minute);
    }

    public override void Write(Utf8JsonWriter writer, Time value, JsonSerializerOptions options)
    {
        writer.WriteStartObject();
        writer.WriteNumber("hour", value.Hour);
        writer.WriteNumber("minute", value.Minute);
        writer.WriteEndObject();
    }
}

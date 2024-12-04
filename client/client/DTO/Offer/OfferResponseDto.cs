﻿using client.DTO;

namespace client.DTO.Offer;

public class OfferResponseDto
{
    public string Id { get; set; }
    public string Title { get; set; }
    public string Description { get; set; }
    public int OriginalPrice { get; set; }
    public int OfferPrice { get; set; }
    public int NumberOfItems { get; set; }
    public int NumberOfAvailableItems { get; set; }
    public DateTime PickupTimeStart { get; set; }
    public DateTime PickupTimeEnd { get; set; }
    public List<string> Categories { get; set; }
    public string ImagePath { get; set; }
    public string Status { get; set; }
}
﻿namespace client.Pages;

public class OfferResponseDto
{
    public string Title { get; set; }
    public string Description { get; set; }
    public int OriginalPrice { get; set; }
    public int OfferPrice { get; set; }
    public int NumberOfItems { get; set; }
    public PickupDate? PickupDate { get; set; }
    public PickupTime? PickupTimeStart { get; set; }
    public PickupTime? PickupTimeEnd { get; set; }
    public List<string> Categories { get; set; }
    public byte? ImagePath { get; set; }
}
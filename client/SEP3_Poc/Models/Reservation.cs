namespace SEP3_Poc.Entities;

public class Reservation
{
    public int ItemId { get; set; }
    public int Quantity { get; set; }
    public decimal Price { get; set; }
    public decimal Total { get; set; }
}
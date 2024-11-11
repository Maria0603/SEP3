namespace SEP3_Poc.Pages
{
    public class FoodBag
    {
        public int Id { get; set; }
        public string Type { get; set; }
        public short OldPrice { get; set; }
        public short NewPrice { get; set; }
        public string Address { get; set; }
        public string PickupTime1 { get; set; }
        public string PickupTime2 { get; set; }
        public string PickupDate { get; set; }
        public string Collector { get; set; }
    }
}
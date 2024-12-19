<<<<<<<< HEAD:client/client/Models/FoodBag.cs
namespace client.Pages
{
========
namespace SEP3_Poc.Entities;
>>>>>>>> sashas_branch:client/SEP3_Poc/Models/FoodBag.cs
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
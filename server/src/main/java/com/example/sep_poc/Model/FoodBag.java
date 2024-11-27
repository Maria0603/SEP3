package com.example.sep_poc.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "extra_food_bag")
public class FoodBag {
  @Id
  private String id ;
  private String type;
  private String address;
  private String pickupTime1;
  private String pickupTime2;
  private String pickupDate;
  private String collector;
  private short price;

  public String getPickupDate()
  {
    return pickupDate;
  }

  public void setPickupDate(String pickupDate)
  {
    this.pickupDate = pickupDate;
  }

  public String getPickupTime2()
  {
    return pickupTime2;
  }

  public void setPickupTime2(String pickupTime2)
  {
    this.pickupTime2 = pickupTime2;
  }

  public String getCollector()
  {
    return collector;
  }

  public void setCollector(String collector)
  {
    this.collector = collector;
  }


  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public String getAddress()
  {
    return address;
  }

  public void setAddress(String address)
  {
    this.address = address;
  }

  public String getPickupTime1()
  {
    return pickupTime1;
  }

  public void setPickupTime1(String pickupTime1)
  {
    this.pickupTime1 = pickupTime1;
  }

  public short getPrice()
  {
    return price;
  }

  public void setPrice(short price)
  {
    this.price = price;
  }
}



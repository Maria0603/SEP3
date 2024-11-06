package com.example.shared.dao;

//import com.example.sep3.grpc.Time;

import com.example.sep3.grpc.Date;
import com.example.sep3.grpc.Time;

public class OfferDao
{
  private String id, title, description;
  private String[] categories;
  private int price, numberOfFoodBags;

  private Date pickupDate;
  private Time pickupTimeStart;
  private Time pickupTimeEnd;
  private String imagePath;

  public OfferDao(String id, String title, String description, int price,
      Date pickupDate, Time pickupTimeStart, Time pickupTimeEnd,
      String[] categories, int numberOfFoodBags, String imagePath)
  {
    this.id = id;
    this.title = title;
    this.description = description;
    this.price = price;
    this.pickupDate = pickupDate;
    this.pickupTimeStart = pickupTimeStart;
    this.pickupTimeEnd = pickupTimeEnd;
    this.categories = categories;
    this.numberOfFoodBags = numberOfFoodBags;
    this.imagePath = imagePath;
  }
  public String getImagePath()
  {
    return imagePath;
  }
  public void setImagePath(String imagePath)
  {
    this.imagePath = imagePath;
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public String[] getCategories()
  {
    return categories;
  }

  public void setCategories(String[] categories)
  {
    this.categories = categories;
  }

  public int getPrice()
  {
    return price;
  }

  public void setPrice(int price)
  {
    this.price = price;
  }
  public int getNumberOfFoodBags()
  {
    return numberOfFoodBags;
  }

  public void setNumberOfFoodBags(int numberOfFoodBags)
  {
    this.numberOfFoodBags=numberOfFoodBags;
  }

  public Time getPickupTimeEnd()
  {
    return pickupTimeEnd;
  }

  public void setPickupTimeEnd(Time pickupTimeEnd)
  {
    this.pickupTimeEnd = pickupTimeEnd;
  }

  public Time getPickupTimeStart()
  {
    return pickupTimeStart;
  }

  public void setPickupTimeStart(Time pickupTimeStart)
  {
    this.pickupTimeStart = pickupTimeStart;
  }

  public Date getPickupDate()
  {
    return pickupDate;
  }

  public void setPickupDate(Date pickupDate)
  {
    this.pickupDate = pickupDate;
  }

}

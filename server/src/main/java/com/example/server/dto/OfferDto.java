package com.example.server.dto;

import java.util.ArrayList;
import java.util.List;

public class OfferDto {

  private String id;
  private String title;
  private String description;
  private int price;
  private int numberOfFoodBags;
  private DateDto pickupDate;
  private TimeDto pickupTimeStart;
  private TimeDto pickupTimeEnd;
  private ArrayList<String> categories;
  private byte[] image;
  private String status;

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus(String status)
  {
    this.status = status;
  }

  public byte[] getImage()
  {
    return image;
  }

  public void setImage(byte[] image)
  {
    this.image = image;
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

  public ArrayList<String> getCategories()
  {
    return categories;
  }


  public void setCategories(ArrayList<String> categories)
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
    this.numberOfFoodBags = numberOfFoodBags;
  }

  public TimeDto getPickupTimeEnd()
  {
    return pickupTimeEnd;
  }

  public void setPickupTimeEnd(TimeDto pickupTimeEnd)
  {
    this.pickupTimeEnd = pickupTimeEnd;
  }

  public TimeDto getPickupTimeStart()
  {
    return pickupTimeStart;
  }

  public void setPickupTimeStart(TimeDto pickupTimeStart)
  {
    this.pickupTimeStart = pickupTimeStart;
  }

  public DateDto getPickupDate()
  {
    return pickupDate;
  }

  public void setPickupDate(DateDto pickupDate)
  {
    this.pickupDate = pickupDate;
  }


}

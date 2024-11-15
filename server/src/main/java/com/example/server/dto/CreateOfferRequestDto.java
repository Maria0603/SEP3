package com.example.server.dto;

import java.util.ArrayList;

public class CreateOfferRequestDto
{
  private String title, description;
  private ArrayList<String> categories;
  private int originalPrice, offerPrice, numberOfItems;

  private DateDto pickupDate;
  private TimeDto pickupTimeStart;
  private TimeDto pickupTimeEnd;

  private byte[] image;

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

  public int getOriginalPrice()
  {
    return originalPrice;
  }

  public void setOriginalPrice(int originalPrice)
  {
    this.originalPrice = originalPrice;
  }

  public int getNumberOfItems()
  {
    return numberOfItems;
  }

  public void setNumberOfItems(int numberOfItems)
  {
    this.numberOfItems = numberOfItems;
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

  public int getOfferPrice()
  {
    return offerPrice;
  }

  public void setOfferPrice(int offerPrice)
  {
    this.offerPrice = offerPrice;
  }
}

package com.example.server.dto;

public class ShortOfferResponseDto
{

  private String id;
  private String title;
  private int originalPrice;
  private int offerPrice;
  private int numberOfItems;
  private DateDto pickupDate;
  private TimeDto pickupTimeStart;
  private TimeDto pickupTimeEnd;
  private String imagePath;
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

  public String getImagePath()
  {
    return imagePath;
  }

  public void setImagePath(String imagePath)
  {
    this.imagePath = imagePath;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
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

package com.example.server.dao;

import java.time.LocalDate;
import java.time.LocalTime;

public class OfferDao
{
  private String id, title, description;
  private String[] categories;
  private int price;
  private LocalDate pickupDate;
  private LocalTime pickupTimeStart;
  private LocalTime pickupTimeEnd;

  public OfferDao(String id, String title, String description, int price,
      LocalDate pickupDate, LocalTime pickupTimeStart, LocalTime pickupTimeEnd,
      String[] categories)
  {
    this.id = id;
    this.title = title;
    this.description = description;
    this.price = price;
    this.pickupDate = pickupDate;
    this.pickupTimeStart = pickupTimeStart;
    this.pickupTimeEnd = pickupTimeEnd;
    this.categories = categories;
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

  public LocalTime getPickupTimeEnd()
  {
    return pickupTimeEnd;
  }

  public void setPickupTimeEnd(LocalTime pickupTimeEnd)
  {
    this.pickupTimeEnd = pickupTimeEnd;
  }

  public LocalTime getPickupTimeStart()
  {
    return pickupTimeStart;
  }

  public void setPickupTimeStart(LocalTime pickupTimeStart)
  {
    this.pickupTimeStart = pickupTimeStart;
  }

  public LocalDate getPickupDate()
  {
    return pickupDate;
  }

  public void setPickupDate(LocalDate pickupDate)
  {
    this.pickupDate = pickupDate;
  }

}

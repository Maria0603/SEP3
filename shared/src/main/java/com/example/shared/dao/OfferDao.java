package com.example.shared.dao;

import com.example.sep3.grpc.Date;
import com.example.sep3.grpc.Time;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "offer") public class OfferDao
{
  @Id private String id;
  @Field("title") private String title;
  @Field("description") private String description;
  @Field("status") private String status;
  @Field("categories") private ArrayList<String> categories;
  @Field("original_price") private int originalPrice;
  @Field("offer_price") private int offerPrice;
  @Field("number_of_items") private int numberOfItems;
  @Field("number_of_available_items") private int numberOfAvailableItems;
  @Field("pickup_date") private DateDao pickupDate;
  @Field("pickup_time_start") private TimeDao pickupTimeStart;
  @Field("pickup_time_end") private TimeDao pickupTimeEnd;
  @Field("image_path") private String imagePath;

  public int getNumberOfAvailableItems()
  {
    return numberOfAvailableItems;
  }

  public void setNumberOfAvailableItems(int numberOfAvailableItems)
  {
    this.numberOfAvailableItems = numberOfAvailableItems;
  }

  public int getOfferPrice()
  {
    return offerPrice;
  }

  public void setOfferPrice(int offerPrice)
  {
    this.offerPrice = offerPrice;
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

  public OfferDao()
  {

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

  public String getStatus()
  {
    return status;
  }

  public void setStatus(String status)
  {
    this.status = status;
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

  public TimeDao getPickupTimeEnd()
  {
    return pickupTimeEnd;
  }

  public void setPickupTimeEnd(TimeDao pickupTimeEnd)
  {
    this.pickupTimeEnd = pickupTimeEnd;
  }

  public TimeDao getPickupTimeStart()
  {
    return pickupTimeStart;
  }

  public void setPickupTimeStart(TimeDao pickupTimeStart)
  {
    this.pickupTimeStart = pickupTimeStart;
  }

  public DateDao getPickupDate()
  {
    return pickupDate;
  }

  public void setPickupDate(DateDao pickupDate)
  {
    this.pickupDate = pickupDate;
  }

}

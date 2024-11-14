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
  @Field("price") private int price;
  @Field("number_of_food_bags") private int numberOfFoodBags;
  @Field("pickup_date") private DateDao pickupDate;
  @Field("pickup_time_start") private TimeDao pickupTimeStart;
  @Field("pickup_time_end") private TimeDao pickupTimeEnd;
  @Field("image_path") private String imagePath;
  //private byte[] image;

  public OfferDao()
  {

  }
/*
  public byte[] getImage()
  {
    return image;
  }

  public void setImage(byte[] image)
  {
    this.image = image;
  }*/

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

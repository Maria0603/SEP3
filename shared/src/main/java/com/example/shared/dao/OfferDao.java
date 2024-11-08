package com.example.shared.dao;

import com.example.sep3.grpc.Date;
import com.example.sep3.grpc.Time;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection="offer")
public class OfferDao
{
  @Id
  private String id;
  private String title, description, status;
  private ArrayList<String> categories;
  private int price, numberOfFoodBags;

  private DateDao pickupDate;
  private TimeDao pickupTimeStart;
  private TimeDao pickupTimeEnd;

  private String imagePath;
  private byte[] image;
  public OfferDao()
  {

  }

  public byte[] getImage()
  {
    return image;
  }

  public void setImage(byte[] image)
  {
    this.image = image;
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

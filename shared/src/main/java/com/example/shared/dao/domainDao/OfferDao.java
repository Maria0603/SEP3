package com.example.shared.dao.domainDao;

import com.example.shared.dao.usersDao.BusinessDao;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Document(collection = "offers") public class OfferDao
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
  @Field("pickup_time_start") private LocalDateTime pickupTimeStart;
  @Field("pickup_time_end") private LocalDateTime pickupTimeEnd;
  @Field("creation_time") private LocalDateTime creationTime;
  @Field("image_path") private String imagePath;
  @DBRef @Field("business_id") private BusinessDao business;

  public BusinessDao getBusiness()
  {
    return business;
  }

  public void setBusiness(BusinessDao business)
  {
    this.business = business;
  }

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

  public LocalDateTime getPickupTimeEnd()
  {
    return pickupTimeEnd;
  }

  public void setPickupTimeEnd(LocalDateTime pickupTimeEnd)
  {
    this.pickupTimeEnd = pickupTimeEnd;
  }

  public LocalDateTime getPickupTimeStart()
  {
    return pickupTimeStart;
  }

  public void setPickupTimeStart(LocalDateTime pickupTimeStart)
  {
    this.pickupTimeStart = pickupTimeStart;
  }

  public LocalDateTime getCreationTime()
  {
    return creationTime;
  }

  public void setCreationTime(LocalDateTime creationTime)
  {
    this.creationTime = creationTime;
  }
}

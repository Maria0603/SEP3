package com.example.shared.dao.domainDao;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "orders") public class OrderDao
{
  @Id private String id;
  @DBRef @Field("offer_id") private OfferDao offer;
  @Field("user_id") private String userId;
  @Field("number_of_items") private int numberOfItems;
  @Field("order_time") private LocalDateTime orderTime;
  @Field("status") private String status;
  @Field("price_per_item") private int pricePerItem;

  public OrderDao(String id, OfferDao offer, String userId, int numberOfItems, LocalDateTime orderTime, String status, int pricePerItem)
  {
    this.id = id;
    this.offer = offer;
    this.userId = userId;
    this.numberOfItems = numberOfItems;
    this.orderTime = orderTime;
    this.status = status;
    this.pricePerItem = pricePerItem;
  }

  public int getPricePerItem()
  {
    return pricePerItem;
  }

  public void setPricePerItem(int pricePerItem)
  {
    this.pricePerItem = pricePerItem;
  }

  public LocalDateTime getOrderTime()
  {
    return orderTime;
  }

  public void setOrderTime(LocalDateTime orderTime)
  {
    this.orderTime = orderTime;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus(String status)
  {
    this.status = status;
  }

  public OrderDao()
  {
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public OfferDao getOffer()
  {
    return offer;
  }

  public void setOffer(OfferDao offer)
  {
    this.offer = offer;
  }

  public String getUserId()
  {
    return userId;
  }

  public void setUserId(String userId)
  {
    this.userId = userId;
  }

  public int getNumberOfItems()
  {
    return numberOfItems;
  }

  public void setNumberOfItems(int numberOfItems)
  {
    this.numberOfItems = numberOfItems;
  }

}
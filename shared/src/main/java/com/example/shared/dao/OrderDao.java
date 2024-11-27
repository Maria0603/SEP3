package com.example.shared.dao;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "order") public class OrderDao
{
  @Id private String id;
  @DBRef @Field("offer_id") private OfferDao offer;
  @Field("user_id") private String userId;
  @Field("number_of_items") private int numberOfItems;
  @Field("order_date") private DateDao orderDate;
  @Field("order_time") private TimeDao orderTime;
  @Field("status") private String status;
  @Field("price_per_item") private int pricePerItem;

  public OrderDao(String id, OfferDao offer, String userId, int numberOfItems,
      DateDao orderDate, TimeDao orderTime, String status, int pricePerItem)
  {
    this.id = id;
    this.offer = offer;
    this.userId = userId;
    this.numberOfItems = numberOfItems;
    this.orderDate = orderDate;
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

  public TimeDao getOrderTime()
  {
    return orderTime;
  }

  public void setOrderTime(TimeDao orderTime)
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

  public DateDao getOrderDate()
  {
    return orderDate;
  }

  public void setOrderDate(DateDao OrderDate)
  {
    this.orderDate = OrderDate;
  }
}
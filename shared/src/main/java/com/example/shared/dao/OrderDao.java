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
  @Field("quantity") private int quantity;
  @Field("order_date") private DateDao orderDate;
  @Field("order_time") private TimeDao orderTime;
  @Field("status") private String status;
  @Field("price_per_item") private int pricePerItem;
  @Field("total_price") private int totalPrice;

  public OrderDao(String id, OfferDao offer, String userId, int quantity,
      DateDao orderDate, TimeDao orderTime, String status, int pricePerItem)
  {
    this.id = id;
    this.offer = offer;
    this.userId = userId;
    this.quantity = quantity;
    this.orderDate = orderDate;
    this.orderTime = orderTime;
    this.status = status;
    this.pricePerItem = pricePerItem;
    this.totalPrice = pricePerItem * quantity;
  }

  public int getPricePerItem()
  {
    return pricePerItem;
  }

  public void setPricePerItem(int pricePerItem)
  {
    this.pricePerItem = pricePerItem;
  }

  public int getTotalPrice()
  {
    return totalPrice;
  }

  public void setTotalPrice(int totalPrice)
  {
    this.totalPrice = totalPrice;
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

  public int getQuantity()
  {
    return quantity;
  }

  public void setQuantity(int quantity)
  {
    this.quantity = quantity;
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
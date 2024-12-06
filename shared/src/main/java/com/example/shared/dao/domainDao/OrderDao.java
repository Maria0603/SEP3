package com.example.shared.dao.domainDao;

import com.example.shared.dao.usersDao.BusinessDao;
import com.example.shared.dao.usersDao.CustomerDao;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "orders") public class OrderDao
{
  @Id private String id;
  @DBRef @Field("offer_id") private OfferDao offer;
  @DBRef @Field("customer_id") private CustomerDao customer;
  @DBRef @Field("business_id") private BusinessDao business;
  @Field("number_of_items") private int numberOfItems;
  @Field("order_time") private LocalDateTime orderTime;
  @Field("status") private String status;
  @Field("price_per_item") private int pricePerItem;

  public BusinessDao getBusiness()
  {
    return business;
  }

  public void setBusiness(BusinessDao business)
  {
    this.business = business;
  }

  public OrderDao(String id, OfferDao offer, CustomerDao customer,
      int numberOfItems, LocalDateTime orderTime, String status,
      int pricePerItem)
  {
    this.id = id;
    this.offer = offer;
    this.customer = customer;
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

  public CustomerDao getCustomer()
  {
    return customer;
  }

  public void setCustomer(CustomerDao customer)
  {
    this.customer = customer;
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
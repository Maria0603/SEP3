package com.example.shared.entities.domainEntities;

import com.example.shared.entities.userEntities.Business;
import com.example.shared.entities.userEntities.Customer;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "purchases") public class Purchase
{
  @Id private String id;
  @DBRef @Field("offer_id") private Offer offer;
  @DBRef @Field("customer_id") private Customer customer;
  @DBRef @Field("business_id") private Business business;
  @Field("number_of_items") private int numberOfItems;
  @Field("purchase_time") private LocalDateTime purchaseTime;
  @Field("status") private String status;
  @Field("price_per_item") private int pricePerItem;

  public Business getBusiness()
  {
    return business;
  }

  public void setBusiness(Business business)
  {
    this.business = business;
  }

  public Purchase(String id, Offer offer, Customer customer,
      int numberOfItems, LocalDateTime purchaseTime, String status,
      int pricePerItem)
  {
    this.id = id;
    this.offer = offer;
    this.customer = customer;
    this.numberOfItems = numberOfItems;
    this.purchaseTime = purchaseTime;
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

  public LocalDateTime getPurchaseTime()
  {
    return purchaseTime;
  }

  public void setPurchaseTime(LocalDateTime purchaseTime)
  {
    this.purchaseTime = purchaseTime;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus(String status)
  {
    this.status = status;
  }

  public Purchase()
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

  public Offer getOffer()
  {
    return offer;
  }

  public void setOffer(Offer offer)
  {
    this.offer = offer;
  }

  public Customer getCustomer()
  {
    return customer;
  }

  public void setCustomer(Customer customer)
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
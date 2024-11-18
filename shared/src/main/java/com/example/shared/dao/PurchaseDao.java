package com.example.shared.dao;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import com.example.shared.dao.DateDao;

@Document(collection = "purchase")
public class PurchaseDao {
  @Id
  private String id;

  @DBRef
  @Field("offer_id")
  private OfferDao offer;

  @Field("user_id")
  private String userId;

  @Field("quantity")
  private int quantity;

  @Field("purchase_date")
  private DateDao purchaseDate;

  public PurchaseDao(String id, OfferDao offer, String userId, int quantity, DateDao purchaseDate) {
    this.id = id;
    this.offer = offer;
    this.userId = userId;
    this.quantity = quantity;
    this.purchaseDate = purchaseDate;
  }

  public PurchaseDao() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public OfferDao getOffer() {
    return offer;
  }

  public void setOffer(OfferDao offer) {
    this.offer = offer;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public DateDao getPurchaseDate() {
    return purchaseDate;
  }

  public void setPurchaseDate(DateDao purchaseDate) {
    this.purchaseDate = purchaseDate;
  }
}
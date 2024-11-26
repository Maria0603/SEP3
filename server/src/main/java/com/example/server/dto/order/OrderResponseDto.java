package com.example.server.dto.order;

import com.example.server.dto.date_time.DateDto;
import com.example.server.dto.date_time.TimeDto;
import jakarta.validation.constraints.NotNull;

public class OrderResponseDto {

  @NotNull(message = "Order ID is required.") private String orderId;

  @NotNull(message = "User ID is required.") private String userId;

  @NotNull(message = "Offer ID is required.") private String offerId;

  @NotNull(message = "Quantity is required.") private int quantity;

  @NotNull(message = "Status is required.") private String status;

  @NotNull(message = "Order date is required.") private DateDto orderDate;

  @NotNull(message = "Order time is required.") private TimeDto orderTime;

  @NotNull(message = "Price per item is required.") private int pricePerItem;

  @NotNull(message = "Total order price is required.") private int totalPrice;

  @NotNull(message = "Message is required.") private String message;

  // Getters and setters
  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getOfferId() {
    return offerId;
  }

  public void setOfferId(String offerId) {
    this.offerId = offerId;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public DateDto getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(DateDto orderDate) {
    this.orderDate = orderDate;
  }

  public TimeDto getOrderTime() {
    return orderTime;
  }

  public void setOrderTime(TimeDto orderTime) {
    this.orderTime = orderTime;
  }

  public int getPricePerItem() {
    return pricePerItem;
  }

  public void setPricePerItem(int pricePerItem) {
    this.pricePerItem = pricePerItem;
  }

  public int getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(int totalPrice) {
    this.totalPrice = totalPrice;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
package com.example.server.dto.order;

import com.example.server.dto.date_time.DateDto;
import jakarta.validation.constraints.NotNull;

public class AddOrderResponseDto {

  @NotNull(message = "Order ID is required.") private String orderId;

  @NotNull(message = "User ID is required.") private String userId;

  @NotNull(message = "Offer ID is required.") private String offerId;

  @NotNull(message = "Quantity is required.") private int quantity;

  @NotNull(message = "Status is required.") private String status;

  @NotNull(message = "Order date is required.") private DateDto orderDate;

  @NotNull(message = "Order time is required.") private DateDto orderTime;

  @NotNull(message = "New order price is required.") private int newOrderPrice;

  @NotNull(message = "Old order price is required.") private int oldOrderPrice;

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

  public DateDto getOrderTime() {
    return orderTime;
  }

  public void setOrderTime(DateDto orderTime) {
    this.orderTime = orderTime;
  }

  public int getNewOrderPrice() {
    return newOrderPrice;
  }

  public void setNewOrderPrice(int newOrderPrice) {
    this.newOrderPrice = newOrderPrice;
  }

  public int getOldOrderPrice() {
    return oldOrderPrice;
  }

  public void setOldOrderPrice(int oldOrderPrice) {
    this.oldOrderPrice = oldOrderPrice;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
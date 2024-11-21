package com.example.server.dto.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PlaceOrderRequestDto {
  @NotNull(message = "Invalid offer ID.") private String offerId;

  @NotNull(message = "Invalid number of items.") @Size(min = 1, message = "You must select at least one item.") private int numberOfItems;

  @NotNull(message = "User ID is required.") private String userId;

  public String getOfferId() {
    return offerId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public void setOfferId(String offerId) {
    this.offerId = offerId;
  }

  public int getNumberOfItems() {
    return numberOfItems;
  }

  public void setNumberOfItems(int numberOfItems) {
    this.numberOfItems = numberOfItems;
  }

}


package com.example.server.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PlaceOrderRequestDto
{
  @NotNull(message = "Invalid offer ID.") private String offerId;
  @NotNull(message = "Invalid number of items.")@Size(min = 1, message = "You must select at least one item.") private int numberOfItems;


  public String getOfferId()
  {
    return offerId;
  }

  public void setOfferId(String offerId)
  {
    this.offerId = offerId;
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


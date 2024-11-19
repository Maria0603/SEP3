package com.example.server.dto;

public class PlaceOrderRequestDto
{
  private String offerId;
  private int numberOfItems;

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


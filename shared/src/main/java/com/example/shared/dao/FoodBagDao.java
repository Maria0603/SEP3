package com.example.shared.dao;

public class FoodBagDao
{
  private String id, status;

  public FoodBagDao(String id, String status)
  {
    this.id = id;
    this.status = status;
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus(String status)
  {
    this.status = status;
  }

}

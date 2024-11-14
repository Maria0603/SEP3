package com.example.shared.model;

public enum Status
{
  AVAILABLE("available"), RESERVED("reserved");
  private final String status;

  Status(String status)
  {
    this.status = status;
  }

  public String getStatus()
  {
    return status;
  }

}


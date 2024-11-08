package com.example.shared.model;

public enum Status
{
  AVAILABLE("available"), RESERVED("reserved");
  private final String status;

  private Status(String status)
  {
    this.status = status;
  }

  public String getStatus()
  {
    return status;
  }

}


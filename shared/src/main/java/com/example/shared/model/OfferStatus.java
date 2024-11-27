package com.example.shared.model;

public enum OfferStatus
{
  AVAILABLE("available"), RESERVED("reserved");
  private final String status;

  OfferStatus(String status)
  {
    this.status = status;
  }

  public String getStatus()
  {
    return status;
  }

}


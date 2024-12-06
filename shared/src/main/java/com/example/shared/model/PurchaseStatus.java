package com.example.shared.model;

public enum PurchaseStatus
{
  PENDING("pending"), RESERVED("reserved"), COMPLETED("completed");
  private final String status;

  PurchaseStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }
}

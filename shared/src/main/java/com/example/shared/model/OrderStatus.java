package com.example.shared.model;

public enum OrderStatus {
  PENDING("pending"), RESERVED("reserved"), COMPLETED("completed");
  private final String status;

  OrderStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }
}

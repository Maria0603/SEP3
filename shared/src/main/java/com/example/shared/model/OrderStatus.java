package com.example.shared.model;

public enum OrderStatus {
  RESERVED("reserved"), PICKED("picked"), NOT_PICKED("not_picked");
  private final String status;

  OrderStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }
}

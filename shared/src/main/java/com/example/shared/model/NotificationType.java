package com.example.shared.model;

public enum NotificationType
{
  PURCHASE("purchase");
  private final String type;

  NotificationType(String type)
  {
    this.type = type;
  }

  public String getType()
  {
    return type;
  }
}

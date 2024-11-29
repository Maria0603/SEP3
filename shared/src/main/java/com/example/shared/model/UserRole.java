package com.example.shared.model;

public enum UserRole
{
  BUSINESS("business"), CUSTOMER("customer");
  private final String roleName;

  UserRole(String roleName)
  {
    this.roleName = roleName;
  }

  public String getRoleName()
  {
    return roleName;
  }

}

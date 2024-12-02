package com.example.shared.model;

public enum UserRole
{
  BUSINESS("BUSINESS"), CUSTOMER("CUSTOMER");
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

package com.example.server.dto.auth;

public class LoginRequestDto
{
  private String email;
  private String password;

  public LoginRequestDto()
  {
  }

  public String getPassword()
  {
    return password;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

}

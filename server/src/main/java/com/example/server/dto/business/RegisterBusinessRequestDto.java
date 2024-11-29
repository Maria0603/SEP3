package com.example.server.dto.business;

import com.example.server.dto.address.AddressDto;

public class RegisterBusinessRequestDto
{
  private String businessName;
  private String cvr;
  private String email;
  private String password;
  private String phoneNumber;
  private AddressDto address;
  private byte[] logo;

  public byte[] getLogo()
  {
    return logo;
  }

  public void setLogo(byte[] logo)
  {
    this.logo = logo;
  }

  public AddressDto getAddress()
  {
    return address;
  }

  public void setAddress(AddressDto address)
  {
    this.address = address;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber)
  {
    this.phoneNumber = phoneNumber;
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

  public String getCvr()
  {
    return cvr;
  }

  public void setCvr(String cvr)
  {
    this.cvr = cvr;
  }

  public String getBusinessName()
  {
    return businessName;
  }

  public void setBusinessName(String businessName)
  {
    this.businessName = businessName;
  }

}

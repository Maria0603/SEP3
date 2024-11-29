package com.example.shared.dao;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "business") public class BusinessDao
{
  @Id private String id;
  @Field("business_name") private String businessName;
  @Field("email") private String email;
  @Field("phone_number") private String phoneNumber;
  @Field("cvr") private String cvr;
  @Field("logo_path") private String logoPath;
  @Field("hashed_password") private String hashedPassword;
  @Field("address") private AddressDao address;


  public AddressDao getAddress()
  {
    return address;
  }

  public void setAddress(AddressDao address)
  {
    this.address = address;
  }

  public String getHashedPassword()
  {
    return hashedPassword;
  }

  public void setHashedPassword(String hashedPassword)
  {
    this.hashedPassword = hashedPassword;
  }

  public String getLogoPath()
  {
    return logoPath;
  }

  public void setLogoPath(String logoPath)
  {
    this.logoPath = logoPath;
  }

  public String getCvr()
  {
    return cvr;
  }

  public void setCvr(String cvr)
  {
    this.cvr = cvr;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber)
  {
    this.phoneNumber = phoneNumber;
  }

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public String getBusinessName()
  {
    return businessName;
  }

  public void setBusinessName(String businessName)
  {
    this.businessName = businessName;
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }



}

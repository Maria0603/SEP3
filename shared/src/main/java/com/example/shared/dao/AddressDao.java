package com.example.shared.dao;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "address") public class AddressDao
{
  @Id private String id;
  @Field("street") private String street;
  @Field("house_number") private String houseNumber;
  @Field("city") private String city;
  @Field("postal_code") private String postalCode;
  @Field("state") private String state;
  @Field("country") private String country;
  @Field("county") private String county;


  public String getCounty()
  {
    return county;
  }

  public void setCounty(String county)
  {
    this.county = county;
  }

  public String getCountry()
  {
    return country;
  }

  public void setCountry(String country)
  {
    this.country = country;
  }

  public String getState()
  {
    return state;
  }

  public void setState(String state)
  {
    this.state = state;
  }

  public String getPostalCode()
  {
    return postalCode;
  }

  public void setPostalCode(String postalCode)
  {
    this.postalCode = postalCode;
  }

  public String getCity()
  {
    return city;
  }

  public void setCity(String city)
  {
    this.city = city;
  }

  public String getHouseNumber()
  {
    return houseNumber;
  }

  public void setHouseNumber(String houseNumber)
  {
    this.houseNumber = houseNumber;
  }

  public String getStreet()
  {
    return street;
  }

  public void setStreet(String street)
  {
    this.street = street;
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

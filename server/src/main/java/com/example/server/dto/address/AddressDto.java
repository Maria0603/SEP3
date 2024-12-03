package com.example.server.dto.address;

import jakarta.validation.constraints.*;

public class AddressDto
{

  @NotBlank(message = "Street cannot be empty.") private String street;

  @NotBlank(message = "Number cannot be empty.") private String number;

  @NotBlank(message = "City cannot be empty.") private String city;

  @NotBlank(message = "County cannot be empty.") private String county;

  @NotBlank(message = "State cannot be empty.") private String state;

  @NotBlank(message = "Country cannot be empty.") private String country;

  @NotBlank(message = "Postal code cannot be empty.") private String postalCode;

  // Getters and setters

  public String getStreet()
  {
    return street;
  }

  public void setStreet(String street)
  {
    this.street = street;
  }

  public String getNumber()
  {
    return number;
  }

  public void setNumber(String number)
  {
    this.number = number;
  }

  public String getCity()
  {
    return city;
  }

  public void setCity(String city)
  {
    this.city = city;
  }

  public String getCounty()
  {
    return county;
  }

  public void setCounty(String county)
  {
    this.county = county;
  }

  public String getState()
  {
    return state;
  }

  public void setState(String state)
  {
    this.state = state;
  }

  public String getCountry()
  {
    return country;
  }

  public void setCountry(String country)
  {
    this.country = country;
  }

  public String getPostalCode()
  {
    return postalCode;
  }

  public void setPostalCode(String postalCode)
  {
    this.postalCode = postalCode;
  }
}

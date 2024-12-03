package com.example.server.dto.business;

import com.example.server.dto.address.AddressDto;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

public class RegisterBusinessRequestDto
{

  @NotBlank(message = "Business name cannot be empty.") @Size(min = 2, max = 50, message = "Business name must be between 2 and 50 characters.") private String businessName;

  @NotBlank(message = "CVR cannot be empty.") @Pattern(regexp = "^[0-9]{8}$", message = "CVR must be exactly 8 digits.") private String cvr;

  @NotBlank(message = "Email cannot be empty.") @Email(message = "Email must be a valid email address.") private String email;

  @NotBlank(message = "Password cannot be empty.") @Size(min = 8, message = "Password must be at least 8 characters long.") private String password;

  @NotBlank(message = "Phone number cannot be empty.") @Size(min = 6, max = 20, message = "Phone number must be between 6 and 20 characters.") private String phoneNumber;

  @Valid
  // Validates the AddressDto inside this field if it has its own validation
  private AddressDto address;

  @NotNull(message = "Logo is required.") private byte[] logo;

  public RegisterBusinessRequestDto()
  {
  }

  // Getters and setters

  public String getBusinessName()
  {
    return businessName;
  }

  public void setBusinessName(String businessName)
  {
    this.businessName = businessName;
  }

  public String getCvr()
  {
    return cvr;
  }

  public void setCvr(String cvr)
  {
    this.cvr = cvr;
  }

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public String getPassword()
  {
    return password;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber)
  {
    this.phoneNumber = phoneNumber;
  }

  public AddressDto getAddress()
  {
    return address;
  }

  public void setAddress(AddressDto address)
  {
    this.address = address;
  }

  public byte[] getLogo()
  {
    return logo;
  }

  public void setLogo(byte[] logo)
  {
    this.logo = logo;
  }
}

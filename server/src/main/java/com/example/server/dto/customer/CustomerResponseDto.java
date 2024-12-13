package com.example.server.dto.customer;

import com.example.server.dto.address.AddressDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDto
{

  private String id;
  private String firstName;
  private String lastName;
  private String phoneNumber;
  private String email;
  private String hashedPassword;
  private AddressDto address;
  private String role;

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber)
  {
    this.phoneNumber = phoneNumber;
  }

  public String getHashedPassword()
  {
    return hashedPassword;
  }

  public void setHashedPassword(String hashedPassword)
  {
    this.hashedPassword = hashedPassword;
  }

  public AddressDto getAddress()
  {
    return address;
  }

  public void setAddress(AddressDto address)
  {
    this.address = address;
  }

  public String getRole()
  {
    return role;
  }

  public void setRole(String role)
  {
    this.role = role;
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



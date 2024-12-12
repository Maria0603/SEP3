package com.example.server.dto.customer;

import com.example.sep3.grpc.CustomerUpdateRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CustomerUpdateRequestDto
{
  String id;


  public String getId()
  {
    return id;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public String getEmail()
  {
    return email;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }

  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public void setPhoneNumber(String phoneNumber)
  {
    this.phoneNumber = phoneNumber;
  }

  @NotBlank(message = "First name cannot be empty.")
  @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters.")
  private String firstName;

  @NotBlank(message = "Last name cannot be empty.")
  @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters.")
  private String lastName;

  @NotBlank(message = "Email cannot be empty.")
  @Email(message = "Email must be a valid email address.")
  private String email;


  @NotBlank(message = "Phone number cannot be empty.")
  @Size(min = 6, max = 20, message = "Phone number must be between 6 and 20 characters.")
  private String phoneNumber;

  public static CustomerUpdateRequest validateAndConvert(
      CustomerUpdateRequestDto requestDto) {
    // Validate the DTO programmatically if needed
    if (requestDto.getFirstName() == null || requestDto.getLastName().isEmpty()) {
      throw new IllegalArgumentException("First name or Last name cannot be empty.");

    }

    // Proceed with conversion
    return CustomerUpdateRequest.newBuilder()
        .setId(requestDto.getId())
        .setFirstName(requestDto.getFirstName())
        .setLastName(requestDto.getLastName())
        .setEmail(requestDto.getEmail())
        .setPhoneNumber(requestDto.getPhoneNumber())
        .build();
  }
}


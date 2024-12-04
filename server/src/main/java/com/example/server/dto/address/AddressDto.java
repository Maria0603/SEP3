package com.example.server.dto.address;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto
{

  @NotBlank(message = "Street cannot be empty.") private String street;

  @NotBlank(message = "Number cannot be empty.") private String number;

  @NotBlank(message = "City cannot be empty.") private String city;

  @NotBlank(message = "County cannot be empty.") private String county;

  @NotBlank(message = "State cannot be empty.") private String state;

  @NotBlank(message = "Country cannot be empty.") private String country;

  @NotBlank(message = "Postal code cannot be empty.") private String postalCode;
}

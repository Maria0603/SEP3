package com.example.server.dto.customer;

import com.example.server.dto.address.AddressDto;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterCustomerRequestDto {

    @NotBlank(message = "First name cannot be empty.")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters.")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty.")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters.")
    private String lastName;

    @NotBlank(message = "Email cannot be empty.")
    @Email(message = "Email must be a valid email address.")
    private String email;

    @NotBlank(message = "Password cannot be empty.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;

    @NotBlank(message = "Phone number cannot be empty.")
    @Size(min = 6, max = 20, message = "Phone number must be between 6 and 20 characters.")
    private String phoneNumber;

    @Valid
    // Validates the AddressDto inside this field if it has its own validation
    private AddressDto address;
}

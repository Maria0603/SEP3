package com.example.server.dto.business;

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
public class RegisterBusinessRequestDto {

    @NotBlank(message = "Business name cannot be empty.")
    @Size(min = 2, max = 50, message = "Business name must be between 2 and 50 characters.")
    private String businessName;

    @NotBlank(message = "CVR cannot be empty.")
    @Pattern(regexp = "^[0-9]{8}$", message = "CVR must be exactly 8 digits.")
    private String cvr;

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

    @NotNull(message = "Logo is required.")
    private byte[] logo;

}

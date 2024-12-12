
package com.example.server.dto.business;

import com.example.sep3.grpc.BusinessUpdateRequest;
import com.example.server.dto.address.AddressDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessUpdateRequestDto {


  @NotBlank(message = "Business name cannot be empty.")
  @Size(min = 2, max = 50, message = "Business name must be between 2 and 50 characters.")
  private String businessName;

  @NotBlank(message = "Email cannot be empty.")
  @Email(message = "Email must be a valid email address.")
  private String email;

  @NotBlank(message = "Phone number cannot be empty.")
  @Size(min = 6, max = 20, message = "Phone number must be between 6 and 20 characters.")
  private String phoneNumber;

  @Valid
  private AddressDto address;

  private String id;

  public static BusinessUpdateRequest validateAndConvert(BusinessUpdateRequestDto requestDto) {
    // Validate the DTO programmatically if needed
    if (requestDto.getBusinessName() == null || requestDto.getBusinessName().isEmpty()) {
      throw new IllegalArgumentException("Business name cannot be empty.");
    }

    // Proceed with conversion
    return BusinessUpdateRequest.newBuilder()
        .setId(requestDto.getId())
        .setBusinessName(requestDto.getBusinessName())
        .setEmail(requestDto.getEmail())
        .setPhoneNumber(requestDto.getPhoneNumber())
        .build();
  }


}

package com.example.server.dto.business;


import com.example.server.dto.address.AddressDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessResponseDto {
    private String id;
    private String email;
    private String phoneNumber;
    private String hashedPassword;
    private AddressDto address;
    private String logo_path;
    private String cvr;
    private String businessName;
    private String role;
}

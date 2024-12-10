package com.example.server.dto.business;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessUpdateRequestDto
{
  String id;
  String email;
  String phoneNumber;
  String businessName;
 // String logoPath;
}

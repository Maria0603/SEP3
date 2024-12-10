package com.example.server.services;

import com.example.server.dto.business.BusinessResponseDto;
import com.example.server.dto.business.BusinessUpdateRequestDto;

public interface IBusinessService {

    BusinessResponseDto getBusinessById(String id);
    BusinessResponseDto updateBusinessProfile(
        BusinessUpdateRequestDto updatedProfile);
}

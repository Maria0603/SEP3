package com.example.server.services;

import com.example.server.dto.business.BusinessResponseDto;
import com.example.server.dto.business.BusinessUpdateRequestDto;
import com.example.server.dto.customer.CustomerLocationRequestResponseDto;

import java.io.IOException;
import java.util.List;

public interface IBusinessService {

    BusinessResponseDto getBusinessById(String id);
    BusinessResponseDto updateAndValidateBusinessProfile(
        BusinessUpdateRequestDto updatedProfile) throws IOException;
    List<BusinessResponseDto> getBusinesses();
}

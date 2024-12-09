package com.example.server.services;

import com.example.server.dto.business.BusinessResponseDto;

public interface IBusinessService {

    BusinessResponseDto getBusinessById(String id);
}

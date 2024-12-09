package com.example.server.services.auxServices;

import com.example.server.dto.address.AddressDto;

import java.util.Map;

public interface IGeocodingService
{
  Map<String, Double> geocodeAddress(AddressDto addressDto);
}

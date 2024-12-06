package com.example.server.services.auxServices;

import com.example.server.config.OpenCageConfig;
import com.example.server.dto.address.AddressDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeocodingService {

  private static final String OPENCAGE_URL = "https://api.opencagedata.com/geocode/v1/json";

  private final RestTemplate restTemplate;
  private final OpenCageConfig openCageConfig;

  @Autowired
  public GeocodingService(RestTemplate restTemplate, OpenCageConfig openCageConfig) {
    this.restTemplate = restTemplate;
    this.openCageConfig = openCageConfig;
  }

  public Map<String, Double> geocodeAddress(AddressDto addressDto) {
    String formattedAddress = formatAddress(addressDto);

    // Build the URL
    String url = UriComponentsBuilder.fromHttpUrl(OPENCAGE_URL)
        .queryParam("q", formattedAddress)
        .queryParam("key", openCageConfig.getApiKey())
        .queryParam("limit", 1)
        .toUriString();

    // Make the API call
    Map<String, Object> response = restTemplate.getForObject(url, Map.class);

    // Parse the response
    if (response == null || !response.containsKey("results")) {
      throw new IllegalArgumentException("Invalid response from OpenCage API");
    }

    List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
    if (results.isEmpty()) {
      throw new IllegalArgumentException("No results found for address: " + formattedAddress);
    }

    // Extract the geometry object
    Map<String, Object> firstResult = results.get(0);
    Map<String, Double> geometry = (Map<String, Double>) firstResult.get("geometry");

    if (geometry == null || !geometry.containsKey("lat") || !geometry.containsKey("lng")) {
      throw new IllegalArgumentException("Invalid geometry data in API response");
    }

    return geometry; // Returns a map with "lat" and "lng"
  }

  private String formatAddress(AddressDto addressDto) {
    // Safely handle null values in address parts
    StringBuilder formattedAddress = new StringBuilder();

    if (addressDto.getStreet() != null) {
      formattedAddress.append(addressDto.getStreet());
      if (addressDto.getNumber() != null) {
        formattedAddress.append(" ").append(addressDto.getNumber());
      }
    }

    appendIfNotNull(formattedAddress, addressDto.getCity());
    appendIfNotNull(formattedAddress, addressDto.getCounty());
    appendIfNotNull(formattedAddress, addressDto.getState());
    appendIfNotNull(formattedAddress, addressDto.getPostalCode());
    appendIfNotNull(formattedAddress, addressDto.getCountry());

    return formattedAddress.toString();
  }

  private void appendIfNotNull(StringBuilder builder, String value) {
    if (value != null && !value.isBlank()) {
      if (builder.length() > 0) {
        builder.append(", ");
      }
      builder.append(value);
    }
  }
}

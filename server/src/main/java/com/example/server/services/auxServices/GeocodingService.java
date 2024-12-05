package com.example.server.services.auxServices;

import com.example.server.config.OpenCageConfig;
import com.example.server.dto.address.AddressDto;
import com.example.server.dto.location.GeoLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service public class GeocodingService
{
  private static final String OPENCAGE_URL = "https://api.opencagedata.com/geocode/v1/json";

  private final RestTemplate restTemplate;
  private com.example.server.config.OpenCageConfig openCageConfig;

  @Autowired public GeocodingService(RestTemplate restTemplate,
      OpenCageConfig openCageConfig)
  {
    this.restTemplate = restTemplate;
    this.openCageConfig = openCageConfig;
  }

  public Map<String, Double> geocodeAddress(AddressDto addressDto)
  {
    String formattedAddress = formatAddress(addressDto);

    // Build the URL
    String url = UriComponentsBuilder.fromHttpUrl(OPENCAGE_URL)
        .queryParam("q", formattedAddress)
        .queryParam("key", openCageConfig.getApiKey()).queryParam("limit", 1)
        .queryParam("pretty", 1).toUriString();

    // Make the request
    Map response = restTemplate.getForObject(url, Map.class);

    // Parse the response
    if (response != null && response.containsKey("results"))
    {
      Map<String, Object> firstResult = ((List<Map<String, Object>>) response.get(
          "results")).get(0);
      Map<String, Double> geometry = (Map<String, Double>) firstResult.get(
          "geometry");
      return geometry; // Returns a map with "lat" and "lng"
    }
    else
    {
      throw new IllegalArgumentException("Unable to geocode address");
    }
  }

  private String formatAddress(AddressDto addressDto)
  {
    return String.join(", ",
        addressDto.getStreet() + " " + addressDto.getNumber(),
        addressDto.getCity(), addressDto.getCounty(), addressDto.getState(),
        addressDto.getPostalCode(), addressDto.getCountry());
  }
}

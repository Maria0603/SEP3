package com.example.server.controllers;

import com.example.server.dto.offer.*;
import com.example.server.security.JWTUtils;
import com.example.server.services.Implementations.OfferService;
import com.example.shared.model.Category;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController @RequestMapping("/offers") public class OfferController
{

  private final OfferService offerService;

  public OfferController(OfferService offerService)
  {
    this.offerService = offerService;
  }

  // Only accessible by BUSINESS
  @PostMapping @PreAuthorize("hasAnyAuthority('BUSINESS')") public ResponseEntity<OfferResponseDto> saveOffer(
      @Valid @RequestBody CreateOfferRequestDto offerRequestDto,
      HttpServletRequest request)
  {
    String userId = (String) request.getAttribute("userId");
    try
    {
      OfferResponseDto offerDto = offerService.createOffer(offerRequestDto,
          userId);
      return ResponseEntity.ok(offerDto);
    }
    catch (Exception e)
    {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  @GetMapping("/categories")
  public List<CategoryDto> getCategories() {
    return List.of(Category.values())
        .stream()
        .map(category -> new CategoryDto(category.getCategoryName(), category.getDirectoryName()))
        .collect(Collectors.toList());
  }

  @GetMapping("/{id}") @PreAuthorize("hasAnyAuthority('BUSINESS', 'CUSTOMER')") public ResponseEntity<OfferResponseDto> getOfferById(
      @PathVariable String id)
  {
    try
    {
      OfferResponseDto offer = offerService.getOfferById(id);
      return ResponseEntity.ok(offer);
    }
    catch (Exception e)
    {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  @GetMapping
  @PreAuthorize("hasAnyAuthority('BUSINESS', 'CUSTOMER')")
  public ResponseEntity<List<OfferResponseDto>> getFilteredOffers(
      @RequestParam(value = "minOfferPrice", required = false) Integer minOfferPrice,
      @RequestParam(value = "maxOfferPrice", required = false) Integer maxOfferPrice,
      @RequestParam(value = "pickupTimeStart", required = false) LocalDateTime pickupTimeStart,
      @RequestParam(value = "pickupTimeEnd", required = false) LocalDateTime pickupTimeEnd,
      @RequestParam(value = "categories", required = false) List<String> categories,
      @RequestParam(value = "latitude", required = false) Double latitude,
      @RequestParam(value = "longitude", required = false) Double longitude,
      @RequestParam(value = "radius", required = false) Double radius,
      @RequestParam(value = "textSearch", required = false) String textSearch) {

    // Create a FilterRequestDto
    FilterRequestDto filterRequestDto = new FilterRequestDto();
    filterRequestDto.setMinOfferPrice(minOfferPrice);
    filterRequestDto.setMaxOfferPrice(maxOfferPrice);
    filterRequestDto.setPickupTimeStart(pickupTimeStart);
    filterRequestDto.setPickupTimeEnd(pickupTimeEnd);
    filterRequestDto.setCategories(categories);
    filterRequestDto.setTextSearch(textSearch);

    // Set location details
    if (latitude != null || longitude != null || radius != null) {
      filterRequestDto.setLocation(new LocationDto(longitude, latitude, radius));
    }
    System.out.println("FilterRequestDto: " + filterRequestDto);
    // Pass the FilterRequestDto to the service
    List<OfferResponseDto> response = offerService.getOffers(filterRequestDto);

    return ResponseEntity.ok(response);
  }
  @GetMapping("/businessOffer/{id}") @PreAuthorize("hasAnyAuthority('BUSINESS', 'CUSTOMER')") public ResponseEntity<List<OfferResponseDto>> getOfferByBusinessId(
      @PathVariable String id)
  {
    try
    {
      var response = offerService.getOffersByBusinessId(id);
      return ResponseEntity.ok(response);
    }
    catch (Exception e)
    {
      throw new IllegalArgumentException(e.getMessage());
    }
  }
}


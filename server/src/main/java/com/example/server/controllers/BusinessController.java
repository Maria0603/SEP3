package com.example.server.controllers;

import com.example.server.dto.business.BusinessResponseDto;
import com.example.server.dto.business.BusinessUpdateRequestDto;
import com.example.server.services.IBusinessService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/businesses") public class BusinessController {


  private final IBusinessService businessService;

  public BusinessController(IBusinessService businessService) {
    this.businessService = businessService;
  }

  @GetMapping("/{id}") public ResponseEntity<BusinessResponseDto> getBusinessById(
      @PathVariable String id)
  {
    try {
      BusinessResponseDto business = businessService.getBusinessById(id);
      return ResponseEntity.ok(business);
    }
    catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping
  public ResponseEntity<List<BusinessResponseDto>> getBusinesses() {
    try {
      List<BusinessResponseDto> businesses = businessService.getBusinesses();
      return ResponseEntity.ok(businesses);
    }
    catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping
  public ResponseEntity<BusinessResponseDto> updateBusinessProfile(@Valid @RequestBody BusinessUpdateRequestDto updatedProfile) {

    // Business logic
    try{
      BusinessResponseDto responseDto = businessService.updateAndValidateBusinessProfile(updatedProfile);
      return ResponseEntity.ok(responseDto);
    }
    catch (Exception e)
    {
      return ResponseEntity.badRequest().build();
    }
  }
}

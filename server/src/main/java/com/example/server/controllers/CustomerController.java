package com.example.server.controllers;


import com.example.server.dto.business.BusinessInRadiusResponseDto;
import com.example.server.dto.customer.CustomerLocationRequestResponseDto;
import com.example.server.dto.customer.CustomerResponseDto;
import com.example.server.dto.customer.CustomerUpdateRequestDto;
import com.example.server.services.ICustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.FieldError;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController @RequestMapping("/customers") public class CustomerController
{

  private final ICustomerService customerService;

  public CustomerController(ICustomerService customerService)
  {
    this.customerService = customerService;
  }
  @GetMapping("/{id}") public ResponseEntity<CustomerResponseDto> getCustomerById(
      @PathVariable String id)
  {
    try {
      CustomerResponseDto customer = customerService.getCustomerById(id);
      return ResponseEntity.ok(customer);
    }
    catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("/location") public ResponseEntity<List<BusinessInRadiusResponseDto>> updateCustomerLocation(
      @RequestBody CustomerLocationRequestResponseDto dto, HttpServletRequest request)
  {
    String userId = (String) request.getAttribute("userId");
    System.out.println("Id:.................: " + userId);
    List<BusinessInRadiusResponseDto> businessesInRadius = customerService.updateCustomerLocation(
        dto, userId);
    return ResponseEntity.ok(businessesInRadius);
  }
  @GetMapping("/location") public ResponseEntity<CustomerLocationRequestResponseDto> getCustomerLocation(HttpServletRequest request)
  {
    String userId = (String) request.getAttribute("userId");
    System.out.println("Id:.................: " + userId);
    CustomerLocationRequestResponseDto location = customerService.getCustomerLocation(userId);
    return ResponseEntity.ok(location);
  }

  @GetMapping("/radius-businesses")  @PreAuthorize("hasAnyAuthority('BUSINESS', 'CUSTOMER', 'ADMIN')")public ResponseEntity<List<BusinessInRadiusResponseDto>> getBusinessesInRadius(HttpServletRequest request)
  {
    String userId = (String) request.getAttribute("userId");
    System.out.println("Id:.................: " + userId);
    List<BusinessInRadiusResponseDto> businessesInRadius = customerService.getBusinessesInRadius(userId);
    return ResponseEntity.ok(businessesInRadius);
  }

  @PutMapping
  public ResponseEntity<?> updateCustomerProfile(
      @Valid @RequestBody CustomerUpdateRequestDto updatedProfile,
      BindingResult bindingResult) {

    // Check for validation errors
    if (bindingResult.hasErrors()) {
      // Extract validation errors and return a structured response
      Map<String, String> errors = bindingResult.getFieldErrors()
          .stream()
          .collect(Collectors.toMap(
              FieldError::getField,
              FieldError::getDefaultMessage,
              (existing, replacement) -> existing // Handle duplicate keys
          ));
      return ResponseEntity.badRequest().body(errors);
    }

    try {
      // Business logic
      CustomerResponseDto responseDto = customerService.updateCustomerProfile(updatedProfile);
      return ResponseEntity.ok(responseDto);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Map.of("error", "An unexpected error occurred while updating the profile."));
    }
  }
}

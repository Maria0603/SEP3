package com.example.server.contollers;

import com.example.server.dto.business.BusinessesInRadiusRequestDto;
import com.example.server.dto.business.BusinessesInRadiusResponseDto;
import com.example.server.services.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController @RequestMapping("/customers") public class CustomerController
{

  private final CustomerService customerService;

  public CustomerController(CustomerService customerService)
  {
    this.customerService = customerService;
  }

  @PostMapping("/map") public ResponseEntity<List<BusinessesInRadiusResponseDto>> getBusinessesInRadius(
      BusinessesInRadiusRequestDto request)
  {
    List<BusinessesInRadiusResponseDto> businessesInRadius = customerService.updateCustomerLocation(request);
    return ResponseEntity.ok(businessesInRadius);
  }

}

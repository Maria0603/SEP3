package com.example.server.contollers;

import com.example.server.dto.business.BusinessInRadiusResponseDto;
import com.example.server.dto.customer.CustomerLocationRequestResponseDto;
import com.example.server.services.ICustomerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/customers") public class CustomerController
{

  private final ICustomerService customerService;

  public CustomerController(ICustomerService customerService)
  {
    this.customerService = customerService;
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

}

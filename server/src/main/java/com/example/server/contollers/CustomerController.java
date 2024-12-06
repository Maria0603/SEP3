package com.example.server.contollers;

import com.example.sep3.grpc.CustomerLocationRequest;
import com.example.server.dto.business.BusinessInRadiusResponseDto;
import com.example.server.dto.customer.CustomerLocationRequestDto;
import com.example.server.services.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/customers") public class CustomerController
{

  private final CustomerService customerService;

  public CustomerController(CustomerService customerService)
  {
    this.customerService = customerService;
  }

  @PostMapping("/location") public ResponseEntity<List<BusinessInRadiusResponseDto>> updateCustomerLocation(
      @RequestBody CustomerLocationRequestDto dto, HttpServletRequest request)
  {
    String userId = (String) request.getAttribute("userId");
    System.out.println("Id:.................: " + userId);
    List<BusinessInRadiusResponseDto> businessesInRadius = customerService.updateCustomerLocation(
        dto, userId);
    return ResponseEntity.ok(businessesInRadius);
  }

  @GetMapping("/radius-businesses")  @PreAuthorize("hasAnyAuthority('BUSINESS', 'CUSTOMER', 'ADMIN')")public ResponseEntity<List<BusinessInRadiusResponseDto>> getBusinessesInRadius(HttpServletRequest request)
  {
    String userId = (String) request.getAttribute("userId");
    System.out.println("Id:.................: " + userId);
    List<BusinessInRadiusResponseDto> businessesInRadius = customerService.getBusinessesInRadius(userId);
    return ResponseEntity.ok(businessesInRadius);
  }

}

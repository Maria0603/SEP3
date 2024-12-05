package com.example.server.contollers;

import com.example.sep3.grpc.CustomerLocationRequest;
import com.example.server.dto.business.BusinessInRadiusResponseDto;
import com.example.server.dto.customer.CustomerLocationRequestDto;
import com.example.server.services.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
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
    String email = (String) request.getAttribute("email");
    System.out.println("Email:.................: " + email);
    List<BusinessInRadiusResponseDto> businessesInRadius = customerService.updateCustomerLocation(
        dto, email);
    return ResponseEntity.ok(businessesInRadius);
  }

  @GetMapping("/radius-businesses") public ResponseEntity<List<BusinessInRadiusResponseDto>> getBusinessesInRadius(HttpServletRequest request)
  {
    return null;
  }

}

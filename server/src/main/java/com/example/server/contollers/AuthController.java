package com.example.server.contollers;

import com.example.server.dto.auth.CredentialsResponseDto;
import com.example.server.dto.auth.LoginRequestDto;
import com.example.server.dto.auth.RefreshTokenRequest;
import com.example.server.dto.business.RegisterBusinessRequestDto;
import com.example.server.dto.customer.RegisterCustomerRequestDto;
import com.example.server.services.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/auth") @RequiredArgsConstructor public class AuthController
{
  private final IAuthService authService;

  @PostMapping("/registration/business") public ResponseEntity<CredentialsResponseDto> registerBusiness(
      @Valid @RequestBody RegisterBusinessRequestDto registerRequest)
  {
    System.out.println("Request for register business in controller");
    return ResponseEntity.ok(authService.registerBusiness(registerRequest));
  }

  @PostMapping("/registration/customer") public ResponseEntity<CredentialsResponseDto> registerCustomer(
          @Valid @RequestBody RegisterCustomerRequestDto registerRequest)
  {
    System.out.println("Request for register customer in controller");
    return ResponseEntity.ok(authService.registerCustomer(registerRequest));
  }

  @PostMapping("/login") public ResponseEntity<CredentialsResponseDto> logInBusiness(
      @RequestBody LoginRequestDto logInRequest)
  {
    return ResponseEntity.ok(authService.logIn(logInRequest));
  }

  @PostMapping("/refresh") public ResponseEntity<CredentialsResponseDto> refreshToken(
      @RequestBody RefreshTokenRequest refreshTokenRequest)
  {
    return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
  }
}
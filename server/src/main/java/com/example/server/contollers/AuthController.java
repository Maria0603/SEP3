package com.example.server.contollers;

import com.example.sep3.grpc.LoginBusinessRequest;
import com.example.sep3.grpc.LoginBusinessResponse;
import com.example.server.dto.auth.CredentialsResponseDto;
import com.example.server.dto.auth.LoginBusinessRequestDto;
import com.example.server.dto.offer.CreateOfferRequestDto;
import com.example.server.dto.offer.OfferResponseDto;
import com.example.server.dto.offer.ShortOfferResponseDto;
import com.example.server.services.AuthService;
import com.example.server.services.OfferService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth") public class AuthController {

    private final AuthService authService;



    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("login/business")
    public ResponseEntity<CredentialsResponseDto> loginBusiness(@RequestBody LoginBusinessRequestDto loginBusinessRequestDto) {

        try{
            LoginBusinessResponse response = authService.loginBusiness(loginBusinessRequestDto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage()); // TODO: update
        }
    }
}
package com.example.server.controllers;

import com.example.server.dto.purchase.*;
import com.example.server.security.JWTUtils;
import com.example.server.services.IPurchaseService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/purchases") public class PurchaseController
{

  private final IPurchaseService purchaseService;
  private final JWTUtils jwtUtils;

  @Autowired public PurchaseController(IPurchaseService purchaseService, JWTUtils jwtUtils)
  {
    System.out.println("purchase service created");
    this.purchaseService = purchaseService;
    this.jwtUtils = jwtUtils;
  }

  @PostMapping @PreAuthorize("hasAnyAuthority('CUSTOMER')")
  public ResponseEntity<CreatePurchaseSessionResponseDto> createPurchase(
      @RequestBody CreatePurchaseRequestDto purchaseRequest, HttpServletRequest request)
  {
    String userId = (String) request.getAttribute("userId");
    System.out.println("Request for create purchase in controller.");
    try
    {
      CreatePurchaseSessionResponseDto response = purchaseService.createPurchase(
          purchaseRequest, userId);
      return ResponseEntity.ok(response);
    }
    catch (IllegalArgumentException e)
    {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  // Stripe will use this endpoint to tell us if the payment is completed
  @PostMapping("/stripe/webhook") public ResponseEntity<String> handleStripeWebhook(
      @RequestBody String payload,
      @RequestHeader("Stripe-Signature") String sigHeader)
  {
    System.out.println("Stripe Webhook");
    purchaseService.refinePurchase(payload, sigHeader);
    return ResponseEntity.ok("Event received");
  }

  @GetMapping @PreAuthorize("hasAnyAuthority('CUSTOMER', 'BUSINESS')") public ResponseEntity<List<PurchaseResponseDto>> getOrders(HttpServletRequest request)
  {
    String userId = (String) request.getAttribute("userId");

    String authHeader = request.getHeader("Authorization");
    String token = authHeader.substring(7);

    String role = jwtUtils.extractRoles(token).getFirst();

    try
    {
      List<PurchaseResponseDto> purchases = purchaseService.getAllPurchases(userId, role);
      return ResponseEntity.ok(purchases);
    }
    catch (Exception e)
    {
      throw new IllegalArgumentException(e.getMessage());
    }

  }

  @GetMapping("/{id}") public ResponseEntity<PurchaseResponseDto> getPurchaseById(
      @PathVariable String id)
  {
    try
    {
      PurchaseResponseDto purchase = purchaseService.getPurchaseById(id);
      return ResponseEntity.ok(purchase);
    }
    catch (Exception e)
    {
      throw new IllegalArgumentException(e.getMessage());
    }
  }  @GetMapping("/detailed/{id}") public ResponseEntity<DetailedPurchaseResponseDto> getDetailedPurchaseById(
    @PathVariable String id)
{
  try
  {
    DetailedPurchaseResponseDto purchase = purchaseService.getDetailedPurchaseById(id);
    return ResponseEntity.ok(purchase);
  }
  catch (Exception e)
  {
    throw new IllegalArgumentException(e.getMessage());
  }
}

}
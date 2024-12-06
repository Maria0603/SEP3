package com.example.server.contollers;

import com.example.server.dto.order.*;
import com.example.server.services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/orders") public class OrderController
{

  private final OrderService orderService;

  @Autowired public OrderController(OrderService orderService)
  {
    System.out.println("order service created");
    this.orderService = orderService;
  }

  @PostMapping @PreAuthorize("hasAnyAuthority('CUSTOMER', 'ADMIN')") public ResponseEntity<PlaceOrderSessionResponseDto> placeOrder(
      @RequestBody PlaceOrderRequestDto orderRequest, HttpServletRequest request)
  {
    String userId = (String) request.getAttribute("userId");
    System.out.println("Request for place order in controller.");
    try
    {
      PlaceOrderSessionResponseDto response = orderService.placeOrder(
          orderRequest, userId);
      return ResponseEntity.ok(response);
    }
    catch (IllegalArgumentException e)
    {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage()); // TODO: update
    }
  }

  // Stripe will use this endpoint to tell us if the payment is completed
  @PostMapping("/stripe/webhook") public ResponseEntity<String> handleStripeWebhook(
      @RequestBody String payload,
      @RequestHeader("Stripe-Signature") String sigHeader)
  {
    System.out.println("Stripe Webhook");
    orderService.refineOrder(payload, sigHeader);
    return ResponseEntity.ok("Event received");
  }

  @GetMapping @PreAuthorize("hasAnyAuthority('CUSTOMER', 'ADMIN')") public ResponseEntity<List<OrderResponseDto>> getOrders(HttpServletRequest request)
  {
    String userId = (String) request.getAttribute("userId");
    try
    {
      List<OrderResponseDto> orders = orderService.getAllOrders(userId);
      return ResponseEntity.ok(orders);
    }
    catch (Exception e)
    {
      throw new IllegalArgumentException(e.getMessage());
    }

  }

  @GetMapping("/{id}") public ResponseEntity<OrderResponseDto> getOrderById(
      @PathVariable String id)
  {
    try
    {
      OrderResponseDto order = orderService.getOrderById(id);
      return ResponseEntity.ok(order);
    }
    catch (Exception e)
    {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

}
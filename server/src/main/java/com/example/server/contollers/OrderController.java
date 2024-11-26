package com.example.server.contollers;

import com.example.server.dto.order.*;
import com.example.server.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/orders") public class OrderController
{

  private final OrderService orderService;

  @Autowired public OrderController(OrderService orderService)
  {
    System.out.println("order service created");
    this.orderService = orderService;
  }

  @PostMapping public ResponseEntity<PlaceOrderSessionResponseDto> placeOrder(
      @RequestBody PlaceOrderRequestDto orderRequest)
  {
    System.out.println("Request for place order in controller.");
    try
    {
      PlaceOrderSessionResponseDto response = orderService.placeOrder(orderRequest);
      return ResponseEntity.ok(response);
    }
    catch (IllegalArgumentException e)
    {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage()); //TODO: update
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

  @GetMapping public ResponseEntity<String> getOrders()
  {
    return ResponseEntity.ok("orders");
  }


}
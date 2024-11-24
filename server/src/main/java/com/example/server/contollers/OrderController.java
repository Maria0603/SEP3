package com.example.server.contollers;

import com.example.sep3.grpc.OrderStatusRequest;
import com.example.server.dto.order.*;
import com.example.server.services.OrderService;
import com.example.shared.model.OrderStatus;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.server.services.OfferService;

@RestController @RequestMapping("/orders") public class OrderController
{

  private final OrderService orderService;

  @Autowired public OrderController(OrderService orderService)
  {
    System.out.println("order service created");
    this.orderService = orderService;
  }

  @PostMapping public ResponseEntity<PlaceOrderResponseDto> placeOrder(
      @RequestBody PlaceOrderRequestDto orderRequest)
  {
    System.out.println("Request for place order in controller.");
    try
    {
      PlaceOrderResponseDto response = orderService.placeOrder(orderRequest);
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
    orderService.refineOrder(payload, sigHeader);
    return ResponseEntity.ok("Event received");
  }

  @GetMapping public ResponseEntity<String> getOrders()
  {
    return ResponseEntity.ok("orders");
  }


}
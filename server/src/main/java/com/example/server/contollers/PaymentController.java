package com.example.server.contollers;

import com.example.server.dto.PaymentRequestDto;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController @RequestMapping("/payments") public class PaymentController
{

  @PostMapping("/checkout-session") public ResponseEntity<Map<String, String>> createCheckoutSession(
      @RequestBody PaymentRequestDto request)
  {
    try
    {
      // Session parameters
      Map<String, Object> sessionParams = new HashMap<>();
      sessionParams.put("payment_method_types", List.of("card"));
      sessionParams.put("mode", "payment");
      sessionParams.put("success_url", request.getSuccessUrl());
      sessionParams.put("cancel_url", request.getCancelUrl());
      sessionParams.put("line_items", List.of(Map.of("price_data",
          Map.of("currency", "dkk", "product_data",
              Map.of("name", "Offer"), "unit_amount",
              request.getAmount()), "quantity", 1)));

      // Create session
      Session session = Session.create(sessionParams);

      // Return session URL
      Map<String, String> response = new HashMap<>();
      response.put("sessionId", session.getId());
      response.put("url", session.getUrl());
      return ResponseEntity.ok(response);
    }
    catch (StripeException e)
    {
      return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
  }
}

package com.example.server.contollers;

import com.example.server.dto.order.AddOrderRequestDto;
import com.example.server.dto.order.AddOrderResponseDto;
import com.example.server.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.server.services.OfferService;

@RestController @RequestMapping("/orders") public class OrderController {

  private final OrderService orderService;

  @Autowired public OrderController(OrderService orderService) {
    System.out.printf("order service created");
    this.orderService = orderService;
  }

  @PostMapping public ResponseEntity<AddOrderResponseDto> addOrder(
      @RequestBody AddOrderRequestDto orderRequestDto) {

    try {
      AddOrderResponseDto orderDto = orderService.addOrder(orderRequestDto);
      return ResponseEntity.ok(orderDto);
    }
    catch (Exception e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  @GetMapping public ResponseEntity<String> getOrders() {
    return ResponseEntity.ok("orders");
  }
}
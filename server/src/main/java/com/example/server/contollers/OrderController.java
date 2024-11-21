package com.example.server.contollers;

//import com.example.data_server.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.server.services.OfferService;

@RestController @RequestMapping("/orders") public class OrderController {

  /*private final OrderService OrderService;

  @PostMapping public OrderDao addOrder(@RequestBody OrderDao Order) {
    return OrderService.addOrder(Order);
  }*/
}
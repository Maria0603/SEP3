package com.example.server.contollers;

import com.example.shared.dao.PurchaseDao;
//import com.example.data_server.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {
 /* @Autowired
  private PurchaseService purchaseService;

  @PostMapping
  public PurchaseDao addPurchase(@RequestBody PurchaseDao purchase) {
    return purchaseService.addPurchase(purchase);
  }*/
}
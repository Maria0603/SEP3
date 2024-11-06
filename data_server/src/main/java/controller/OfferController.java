package controller;

import model.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.OfferService;

import java.util.List;

@RestController @RequestMapping("/api/offer") public class OfferController
{
  private final OfferService offerService;

  @Autowired public OfferController(OfferService offerService)
  {
    this.offerService = offerService;
  }

  @GetMapping public List<Offer> getAllOffers()
  {
    return offerService.getAllOffers();
  }

  @GetMapping("/{id}") public ResponseEntity<Offer> getOfferById(
      @PathVariable String id)
  {
    return offerService.getOfferById(id).map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping public Offer createOffer(@RequestBody Offer offer)
  {
    return offerService.createOffer(offer);
  }



}

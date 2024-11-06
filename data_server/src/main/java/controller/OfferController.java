package controller;

import model.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.OfferServiceImpl;

import java.util.List;

@RestController @RequestMapping("/api/offer") public class OfferController
{
  private final OfferServiceImpl offerServiceImpl;

  @Autowired public OfferController(OfferServiceImpl offerServiceImpl)
  {
    this.offerServiceImpl = offerServiceImpl;
  }

  @GetMapping public List<Offer> getAllOffers()
  {
    return offerServiceImpl.getAllOffers();
  }

  @GetMapping("/{id}") public ResponseEntity<Offer> getOfferById(
      @PathVariable String id)
  {
    return offerServiceImpl.getOfferById(id).map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping public Offer createOffer(@RequestBody Offer offer)
  {
    return offerServiceImpl.createOffer(offer);
  }



}

package com.example.server.contollers;

import com.example.sep3.grpc.SaveOfferResponse;
import com.example.server.dto.CreateOfferRequestDto;
import com.example.server.services.OfferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/offers") public class OfferController
{
  //private final DataServerStub dataServerStub;

  private final OfferService offerService;

  /*@Autowired*/
  public OfferController(OfferService offerService)
  {
    this.offerService = offerService;
  }

  @PostMapping public ResponseEntity<String> saveOffer(
      @RequestBody CreateOfferRequestDto offerRequestDto)
  {
    try
    {
      //Maybe send the whole object to the client?
      String offerId = offerService.saveOffer(offerRequestDto);

      return ResponseEntity.ok("Offer created with ID: " + offerId);
    }
    catch (Exception e)
    {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }


}
package com.example.server.contollers;

import com.example.server.dto.offer.ShortOfferResponseDto;
import com.example.server.services.OfferService;
import com.example.server.dto.offer.CreateOfferRequestDto;
import com.example.server.dto.offer.OfferResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/offers") public class OfferController
{

  private final OfferService offerService;

  public OfferController(OfferService offerService)
  {
    this.offerService = offerService;

  }

  //Look at OfferTestClient to see how the request should look like
  @PostMapping public ResponseEntity<OfferResponseDto> saveOffer(
      @Valid @RequestBody CreateOfferRequestDto offerRequestDto)
  {
    try
    {
      OfferResponseDto offerDto = offerService.saveOffer(offerRequestDto);
      return ResponseEntity.ok(offerDto);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  @GetMapping public ResponseEntity<List<ShortOfferResponseDto>> getShortAvailableOffers()
  {
    try
    {
      List<ShortOfferResponseDto> offers = offerService.getAvailableOffers();

      return ResponseEntity.ok(offers);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }
  }


}

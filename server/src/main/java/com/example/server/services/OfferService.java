package com.example.server.services;

import com.example.sep3.grpc.*;
import com.example.server.DataServerStub;
import com.example.server.dto.CreateOfferRequestDto;
import com.example.server.converters.DateConverter;
import com.example.server.converters.TimeConverter;
import com.example.server.dto.DateDto;
import com.example.server.dto.TimeDto;
import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

import static com.example.server.converters.DtoGrpcConverter.CreateOfferRequestDto_To_SaveOfferRequest;

@Service public class OfferService extends OfferServiceGrpc.OfferServiceImplBase
{
  private final DataServerStub dataServerStub;

  @Autowired public OfferService(DataServerStub dataServerStub)
  {
    this.dataServerStub = dataServerStub;
    Stripe.apiKey = "sk_test_51QLXFcEJybmJ8DbtUW95vPtVV4vCIHtWi7MgOuqlhLngWoki5Bo0iMF8s2Nfxhzpub5gnIAD3d0CUpZBcSAJanmp004vLU11xd";

    System.out.println("OfferService created");
  }

  public String saveOffer(CreateOfferRequestDto offerRequestDto)
  {
    //First check what we couldn't check in the Dto class
    if(!isPickupInFuture(offerRequestDto.getPickupDate(), offerRequestDto.getPickupTimeEnd()))
      throw new IllegalArgumentException("Pickup date and time must be in the future");


    //Transform the dto to grpc message
    SaveOfferRequest request = CreateOfferRequestDto_To_SaveOfferRequest(
        offerRequestDto);

    //Send the request to the data server
    SaveOfferResponse response = dataServerStub.saveOffer(request);

    //return the id; maybe more?
    return response.getId();
  }

  private boolean isPickupInFuture(DateDto date, TimeDto time)
  {
    LocalDateTime pickupDateTime = LocalDateTime.of(
        LocalDate.of(date.getYear(), date.getMonth(), date.getDay()),
        LocalTime.of(time.getHour(), time.getMinute()));
    return pickupDateTime.isAfter(LocalDateTime.now());
  }

}

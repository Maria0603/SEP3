package com.example.server.services;

import com.example.sep3.grpc.*;
import com.example.server.DataServerStub;
import com.example.server.dto.*;
import com.example.server.converters.DateConverter;
import com.example.server.converters.TimeConverter;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.server.converters.DtoGrpcConverter.CreateOfferRequestDto_To_SaveOfferRequest;

@Service public class OfferService extends OfferServiceGrpc.OfferServiceImplBase
{
  private final DataServerStub dataServerStub;
  @Value("${stripe.success-url}") private String successUrl;

  @Value("${stripe.cancel-url}") private String cancelUrl;

  @Autowired public OfferService(DataServerStub dataServerStub)
  {
    this.dataServerStub = dataServerStub;
    System.out.println("OfferService created");
  }

  public String saveOffer(CreateOfferRequestDto offerRequestDto)
  {
    //First check what we couldn't check in the Dto class
    if (!isPickupInFuture(offerRequestDto.getPickupDate(),
        offerRequestDto.getPickupTimeEnd()))
      throw new IllegalArgumentException(
          "Pickup date and time must be in the future");

    //Transform the dto to grpc message
    SaveOfferRequest request = CreateOfferRequestDto_To_SaveOfferRequest(
        offerRequestDto);

    //Send the request to the data server
    SaveOfferResponse response = dataServerStub.saveOffer(request);

    //return the id; maybe more?
    return response.getId();
  }

  public PlaceOrderResponseDto placeOrder(PlaceOrderRequestDto requestDto)
  {
    try
    {
      // Session parameters
      Map<String, Object> sessionParams = new HashMap<>();
      sessionParams.put("payment_method_types", List.of("card"));
      sessionParams.put("mode", "payment");
      sessionParams.put("success_url", successUrl);
      sessionParams.put("cancel_url", cancelUrl);
      sessionParams.put("line_items", List.of(Map.of("price_data",
          Map.of("currency", "dkk", "product_data", Map.of("name", "Offer"),
              //this is the amount in ore; it must be above 250, as established by Stripe
              "unit_amount", 3400),
          "quantity", 2))); //TODO: Fetch the data from data_server instead

      // Create session
      Session session = Session.create(sessionParams);

      // Return session URL
      PlaceOrderResponseDto response = new PlaceOrderResponseDto();
      response.setUrl(session.getUrl());
      response.setSessionId(session.getId());
      return response;
    }
    catch (StripeException e)
    {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  private boolean isPickupInFuture(DateDto date, TimeDto time)
  {
    LocalDateTime pickupDateTime = LocalDateTime.of(
        LocalDate.of(date.getYear(), date.getMonth(), date.getDay()),
        LocalTime.of(time.getHour(), time.getMinute()));
    return pickupDateTime.isAfter(LocalDateTime.now());
  }

}

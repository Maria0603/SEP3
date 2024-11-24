package com.example.server.services;

import com.example.sep3.grpc.*;
import com.example.server.DataServerStub;
import com.example.server.converters.DtoGrpcConverter;
import com.example.server.dto.*;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.server.converters.DtoGrpcConverter.CreateOfferRequestDto_To_SaveOfferRequest;
import static com.example.server.converters.DtoGrpcConverter.SaveOfferResponseGrpc_To_OfferResponseDto;

@Service public class OfferService extends OfferServiceGrpc.OfferServiceImplBase
{
  private final DataServerStub dataServerStub;
  private final ImageStorageService imageStorageService;
  @Value("${stripe.success-url}") private String successUrl; // from application.properties
  @Value("${stripe.cancel-url}") private String cancelUrl;

  @Autowired public OfferService(DataServerStub dataServerStub,
      ImageStorageService imageStorageService)
  {
    this.dataServerStub = dataServerStub;
    this.imageStorageService = imageStorageService;
    System.out.println("OfferService created");
  }

  @Transactional public OfferResponseDto saveOffer(
      CreateOfferRequestDto offerRequestDto)
  {
    //First check what we couldn't check in the Dto class
    validateOfferDetails(offerRequestDto);
    String imagePath = null;
    try
    {
      imagePath = imageStorageService.getBaseDirectory()
          + imageStorageService.saveImage(offerRequestDto.getImage());
      //Transform the dto to grpc message
      SaveOfferRequest request = CreateOfferRequestDto_To_SaveOfferRequest(
          offerRequestDto, imagePath);

      //Send the request to the data server
      SaveOfferResponse response = dataServerStub.saveOffer(request);

      //Return the offer as dto
      return SaveOfferResponseGrpc_To_OfferResponseDto(response);
    }
    catch (IOException e)
    {
      e.printStackTrace();
      if (imagePath != null)
        imageStorageService.deleteImage(imagePath); //rollback
      throw new IllegalArgumentException("Failed to save the image");
    }

  }

  public List<ShortOfferResponseDto> getAvailableOffers()
  {
    //Build the grpc request
    EmptyMessage request = EmptyMessage.newBuilder().build();

    //Send the request to the data server
    OfferList response = dataServerStub.getAvailableOffers(request);

    ArrayList<ShortOfferResponseDto> offers = new ArrayList<>();
    for (int i = 0; i < response.getOfferCount(); i++)
    {
      ShortOfferResponseDto dto = DtoGrpcConverter.ShortOfferResponseGrpc_To_ShortOfferResponseDto(
          response.getOffer(i));

      //Attach the images
      String imagePath = response.getOffer(i).getImagePath();
      if (imageExists(imagePath))
        dto.setImagePath(imagePath);
      else
        throw new IllegalArgumentException(
            "Image not found for order ID: " + response.getOffer(i).getId()
                + " with path " + imagePath);
      offers.add(dto);
    }

    return offers;
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
              "unit_amount", 3400), "quantity",
          2))); //TODO: Fetch the data from data_server instead

      // Create session
      Session session = Session.create(sessionParams);

      //Save empty order first

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

  private void validateOfferDetails(CreateOfferRequestDto offerRequestDto)
  {
    if (!isPickupInFuture(offerRequestDto.getPickupDate(),
        offerRequestDto.getPickupTimeEnd()))
      throw new IllegalArgumentException(
          "Pickup date and time must be in the future");

  }

  private boolean isPickupInFuture(DateDto date, TimeDto time)
  {
    LocalDateTime pickupDateTime = LocalDateTime.of(
        LocalDate.of(date.getYear(), date.getMonth(), date.getDay()),
        LocalTime.of(time.getHour(), time.getMinute()));
    return pickupDateTime.isAfter(LocalDateTime.now());
  }

  public boolean imageExists(String filePath)
  {
    File imageFile = new File(filePath);
    return imageFile.exists();
  }

}

package com.example.server.services.Implementations;

import com.example.sep3.grpc.*;
import com.example.server.DataServerStub;
import com.example.server.converters.PurchaseDtoGrpcConverter;
import com.example.server.dto.purchase.PurchaseResponseDto;
import com.example.server.dto.purchase.CreatePurchaseRequestDto;
import com.example.server.dto.purchase.CreatePurchaseSessionResponseDto;
import com.example.server.services.IPurchaseService;
import com.example.server.services.auxServices.Implementations.ImageStorageStorageService;
import com.example.shared.model.PurchaseStatus;
import com.example.shared.model.UserRole;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service public class PurchaseService implements IPurchaseService
{
  private final DataServerStub dataServerStub;
  private final OfferService offerService;

  @Value("${stripe.success-url}") private String successUrl; // from application.properties
  @Value("${stripe.cancel-url}") private String cancelUrl;
  @Value("${stripe.signing.secret}") private String stripeSigningSecret;

  @Autowired public PurchaseService(DataServerStub dataServerStub,
      OfferService offerService)
  {
    this.dataServerStub = dataServerStub;
    this.offerService = offerService;
    System.out.println("PurchaseService created");
  }

  @Override public CreatePurchaseSessionResponseDto createPurchase(
      CreatePurchaseRequestDto requestDto, String userId)
  {

    CreatePurchaseRequest purchaseRequest = PurchaseDtoGrpcConverter.CreatePurchaseRequestDto_To_CreatePurchaseRequest(
        requestDto, userId);

    System.out.println("Request for database built");

    PurchaseResponse databaseResponse = dataServerStub.createPurchase(purchaseRequest);

    /////////////////////////////////////Temporary////////////////////////////////
    updatePurchaseStatus(databaseResponse.getId(),
        PurchaseStatus.RESERVED.getStatus());
    PurchaseResponseDto purchase = getPurchaseById(databaseResponse.getId());
    //////////////////////////////////////////////////////////////////////////////
    if (databaseResponse == null || databaseResponse.getId().isEmpty())
      throw new IllegalArgumentException("Invalid purchase ID.");

    System.out.println("Purchase initially saved in database");

    try
    {
      Map<String, Object> sessionParams = new HashMap<>();
      sessionParams.put("payment_method_types", List.of("card"));
      sessionParams.put("mode", "payment");
      sessionParams.put("success_url", successUrl);
      sessionParams.put("cancel_url", cancelUrl);
      sessionParams.put("line_items", List.of(Map.of("price_data",
          Map.of("currency", "dkk", "product_data", Map.of("name", "Offer"),
              "unit_amount", databaseResponse.getPricePerItem() * 100
              // Stripe requires amounts in the smallest currency unit
          ), "quantity", requestDto.getNumberOfItems())));

      Map<String, Object> metadata = new HashMap<>();
      metadata.put("purchaseId", databaseResponse.getId());

      sessionParams.put("metadata", metadata);

      Session session = Session.create(sessionParams);

      CreatePurchaseSessionResponseDto response = new CreatePurchaseSessionResponseDto();
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

  @Override public void refinePurchase(String payload, String sigHeader)
  {
    System.out.println("payload: " + payload);
    System.out.println("sigHeader: " + sigHeader);

    Event event;
    try
    {
      event = Webhook.constructEvent(payload, sigHeader, stripeSigningSecret);
    }
    catch (SignatureVerificationException e)
    {
      e.printStackTrace();
      throw new IllegalArgumentException("Invalid Signature");
    }

    if ("checkout.session.completed".equals(event.getType()))
    {
      Session session = (Session) event.getDataObjectDeserializer().getObject()
          .orElse(null);

      if (session != null)
      {
        String purchaseId = session.getMetadata().get("purchaseId");
        updatePurchaseStatus(purchaseId, PurchaseStatus.RESERVED.getStatus());
      }
    }
  }

  @Override public List<PurchaseResponseDto> getAllPurchases(String userId, String role)
  {
    System.out.println("getPurchases method called");
    GetPurchaseRequest request = GetPurchaseRequest.newBuilder()
        .setUserId(userId)
        .setRole(role)
        .build();
    PurchaseListResponse response = dataServerStub.getPurchases(request);
    System.out.println("Received response from dataServerStub: " + response);
    return response.getPurchasesList().stream()
        .map(PurchaseDtoGrpcConverter::PurchaseResponse_To_PurchaseResponseDto)
        .collect(Collectors.toList());
  }

  @Override public PurchaseResponseDto getPurchaseById(String id)
  {
    System.out.println("getPurchaseById method called with id: " + id);
    PurchaseIdRequest request = PurchaseIdRequest.newBuilder().setId(id).build();
    PurchaseResponse response = dataServerStub.getPurchaseById(request);
    System.out.println("Received response from dataServerStub: " + response);
    return PurchaseDtoGrpcConverter.PurchaseResponse_To_PurchaseResponseDto(
        response);
  }

  private void updatePurchaseStatus(String purchaseId, String status)
  {
    PurchaseStatusRequest updateRequest = PurchaseStatusRequest.newBuilder()
        .setId(purchaseId).setStatus(status).build();

    dataServerStub.updatePurchaseStatus(updateRequest);
  }

}





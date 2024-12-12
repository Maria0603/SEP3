package com.example.server.services.Implementations;

import com.example.sep3.grpc.*;
import com.example.server.DataServerStub;
import com.example.server.config.RestTemplateConfig;
import com.example.server.converters.NotificationDtoGrpcConverter;
import com.example.server.converters.PurchaseDtoGrpcConverter;
import com.example.server.dto.purchase.DetailedPurchaseResponseDto;
import com.example.server.dto.notifications.NotificationResponseDto;
import com.example.server.dto.purchase.PurchaseResponseDto;
import com.example.server.dto.purchase.CreatePurchaseRequestDto;
import com.example.server.dto.purchase.CreatePurchaseSessionResponseDto;
import com.example.server.services.IPurchaseService;
import com.example.server.services.auxServices.IEmailService;
import com.example.server.services.auxServices.IEventService;
import com.example.shared.converters.DateTimeConverter;
import com.example.shared.model.NotificationType;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service public class PurchaseService implements IPurchaseService
{
  private final DataServerStub dataServerStub;
  private final IEventService eventService;
  private final IEmailService emailService;
  private final RestTemplateConfig restTemplateConfig;

  @Value("${stripe.success-url}") private String successUrl; // from application.properties
  @Value("${stripe.cancel-url}") private String cancelUrl;
  @Value("${stripe.signing.secret}") private String stripeSigningSecret;

  @Autowired public PurchaseService(DataServerStub dataServerStub,
      IEventService eventService, IEmailService emailService,
      RestTemplateConfig restTemplateConfig)
  {
    this.dataServerStub = dataServerStub;
    this.eventService = eventService;
    this.emailService = emailService;
    System.out.println("PurchaseService created");
    this.restTemplateConfig = restTemplateConfig;
  }

  @Override public CreatePurchaseSessionResponseDto createPurchase(
      CreatePurchaseRequestDto requestDto, String userId)
  {

    CreatePurchaseRequest purchaseRequest = PurchaseDtoGrpcConverter.CreatePurchaseRequestDto_To_CreatePurchaseRequest(
        requestDto, userId);

    System.out.println("Request for database built");

    PurchaseResponse databaseResponse = dataServerStub.createPurchase(
        purchaseRequest);

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

      // Notify business
      NotificationRequestResponse businessNotification = NotificationRequestResponse.newBuilder()
          .setContent("A new purchase has been made for your offer #"
              + databaseResponse.getOfferId()).setTimestamp(
              DateTimeConverter.convertLocalDateTime_To_ProtoTimestamp(
                  LocalDateTime.now()))
          .setType(NotificationType.PURCHASE.getType())
          .setUserId(databaseResponse.getBusinessId())
          .setUserRole(UserRole.BUSINESS.getRoleName())
          .setSubjectId(databaseResponse.getOfferId()).build();

      dataServerStub.createNotification(businessNotification);

      //I think we don't have to send anything to the client, but I'll keep these here
      //////////////////////////////////////////////////////////////////////////
      NotificationResponseDto businessDto = NotificationDtoGrpcConverter.NotificationRequestResponse_To_NotificationResponseDto(
          businessNotification);
      //////////////////////////////////////////////////////////////////////////

      eventService.sendNotification(
          businessDto.getUserRole() + businessDto.getUserId(),
          "Notification for " + businessDto.getUserRole()
              + businessDto.getUserId());

      // Notify customer
      NotificationRequestResponse customerNotification = NotificationRequestResponse.newBuilder()
          .setContent("You made a new purchase on offer #"
              + databaseResponse.getOfferId()).setTimestamp(
              DateTimeConverter.convertLocalDateTime_To_ProtoTimestamp(
                  LocalDateTime.now()))
          .setType(NotificationType.PURCHASE.getType())
          .setUserId(databaseResponse.getCustomerId())
          .setUserRole(UserRole.CUSTOMER.getRoleName())
          .setSubjectId(databaseResponse.getOfferId()).build();

      dataServerStub.createNotification(customerNotification);

      //////////////////////////////////////////////////////////////////////////
      NotificationResponseDto customerDto = NotificationDtoGrpcConverter.NotificationRequestResponse_To_NotificationResponseDto(
          customerNotification);
      //////////////////////////////////////////////////////////////////////////

      eventService.sendNotification(
          customerDto.getUserRole() + customerDto.getUserId(),
          "Notification for " + customerDto.getUserRole()
              + customerDto.getUserId());

      emailService.sendNotificationEmail(databaseResponse.getBusinessEmail(),
          "New Purchase", "A new purchase has been made for your offer #"
              + databaseResponse.getOfferId());

      emailService.sendNotificationEmail(databaseResponse.getCustomerEmail(),
          "Purchase Confirmation",
          "The purchase on offer #" + databaseResponse.getOfferId()
              + " has been confirmed!");

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
    PurchaseIdRequest request = PurchaseIdRequest.newBuilder().setId(id)
        .build();
    PurchaseResponse response = dataServerStub.getPurchaseById(request);
    System.out.println("Received response from dataServerStub: " + response);
    return PurchaseDtoGrpcConverter.PurchaseResponse_To_PurchaseResponseDto(
        response);
  }
  @Override public DetailedPurchaseResponseDto getDetailedPurchaseById(String id)
  {
    System.out.println("getPurchaseById method called with id: " + id);
    PurchaseIdRequest request = PurchaseIdRequest.newBuilder().setId(id).build();
    DetailedPurchaseResponse response = dataServerStub.getDetailedPurchaseById(request);
    System.out.println("Received response from dataServerStub: " + response);
    return PurchaseDtoGrpcConverter.PurchaseResponse_To_DetailedPurchaseResponseDto(
            response);

  }
  private void updatePurchaseStatus(String purchaseId, String status)
  {
    PurchaseStatusRequest updateRequest = PurchaseStatusRequest.newBuilder()
        .setId(purchaseId).setStatus(status).build();

    dataServerStub.updatePurchaseStatus(updateRequest);
  }

}





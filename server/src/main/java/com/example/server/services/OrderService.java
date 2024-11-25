package com.example.server.services;

import com.example.sep3.grpc.*;
import com.example.server.DataServerStub;
import com.example.server.converters.DtoGrpcConverter;
import com.example.server.dto.order.AddOrderRequestDto;
import com.example.server.dto.order.AddOrderResponseDto;
import com.example.server.dto.order.PlaceOrderRequestDto;
import com.example.server.dto.order.PlaceOrderSessionResponseDto;
import com.example.shared.model.OrderStatus;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.example.server.dto.order.OrderResponseDto;
import com.google.protobuf.Empty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService extends OrderServiceGrpc.OrderServiceImplBase {
  private final DataServerStub dataServerStub;
  @Value("${stripe.success-url}")
  private String successUrl; // from application.properties
  @Value("${stripe.cancel-url}")
  private String cancelUrl;
  @Value("${stripe.signing.secret}")
  private String stripeSigningSecret;

  @Autowired
  public OrderService(DataServerStub dataServerStub) {
    System.out.println("OrderService created");
    this.dataServerStub = dataServerStub;
  }

  @Transactional
  public AddOrderResponseDto addOrder(
      AddOrderRequestDto orderRequestDto) {
    System.out.println(
        "addOrder method called with request: " + orderRequestDto);
    AddOrderRequest request = DtoGrpcConverter.AddOrderRequestDto_To_AddOrderRequest(
        orderRequestDto);
    System.out.println(
        "Converted AddOrderRequestDto to AddOrderRequest: " + request);
    OrderResponse response = dataServerStub.addOrder(request);
    System.out.println("Received response from dataServerStub: " + response);
    return DtoGrpcConverter.AddOrderResponseGrpc_To_AddOrderResponseDto(
        response);
  }

  public PlaceOrderSessionResponseDto placeOrder(PlaceOrderRequestDto requestDto) {
    // Save order with status "Reserved" first
    AddOrderRequest orderRequest = AddOrderRequest.newBuilder()
        .setOfferId(requestDto.getOfferId())
        .setQuantity(requestDto.getNumberOfItems())
        .setUserId("TestUser123") // not there yet
        .build();

    System.out.println("Request for database built");

    OrderResponse databaseResponse = dataServerStub.addOrder(orderRequest);
    if (databaseResponse.getId() == null || databaseResponse.getId().isEmpty())
      throw new IllegalArgumentException("Invalid order ID.");

    System.out.println("price per item: " + databaseResponse.getPricePerItem());

    System.out.println("Order initially saved in database");
    try {
      // Session parameters
      Map<String, Object> sessionParams = new HashMap<>();
      sessionParams.put("payment_method_types", List.of("card"));
      sessionParams.put("mode", "payment");
      sessionParams.put("success_url", successUrl);
      sessionParams.put("cancel_url", cancelUrl);
      sessionParams.put("line_items", List.of(Map.of("price_data",
          Map.of("currency", "dkk", "product_data", Map.of("name", "Offer"),
              // this is the amount in ore; it must be above 250, as established by Stripe
              "unit_amount", databaseResponse.getPricePerItem() * 100),
          "quantity",
          requestDto.getNumberOfItems()))); // TODO: Fetch the data from data_server instead

      Map<String, Object> metadata = new HashMap<>();
      metadata.put("orderId", databaseResponse.getId());

      sessionParams.put("metadata", metadata);
      // Create session
      Session session = Session.create(sessionParams);

      // Return session URL
      PlaceOrderSessionResponseDto response = new PlaceOrderSessionResponseDto();
      response.setUrl(session.getUrl());
      response.setSessionId(session.getId());
      return response;
    } catch (StripeException e) {
      e.printStackTrace(); // We need a logger guys
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  public void refineOrder(String payload, String sigHeader) {
    System.out.println("payload: " + payload);
    System.out.println("sigHeader: " + sigHeader);

    Event event;
    try {
      event = Webhook.constructEvent(payload, sigHeader, stripeSigningSecret);
    } catch (SignatureVerificationException e) {
      e.printStackTrace();
      throw new IllegalArgumentException("Invalid Signature");
    }

    // Handle the event
    if ("checkout.session.completed".equals(event.getType())) {
      Session session = (Session) event.getDataObjectDeserializer().getObject()
          .orElse(null);

      if (session != null) {
        String orderId = session.getMetadata().get("orderId");

        // Update order status in the database
        updateOrderStatus(orderId, OrderStatus.RESERVED.getStatus());
      }
    }
  }

  public void updateOrderStatus(String orderId, String status) {
    OrderStatusRequest updateRequest = OrderStatusRequest.newBuilder()
        .setId(orderId).setStatus(status).build();

    dataServerStub.updateOrderStatus(updateRequest);
  }

  public List<OrderResponseDto> getAllOrders() {
    System.out.println("getAllOrders method called");
    Empty request = Empty.newBuilder().build();
    OrderList response = dataServerStub.getAllOrders(request);
    System.out.println("Received response from dataServerStub: " + response);
    return response.getOrdersList().stream()
        .map(DtoGrpcConverter::OrderResponseGrpc_To_OrderResponseDto)
        .collect(Collectors.toList());
  }

  public OrderResponseDto getOrderById(String id) {
    System.out.println("getOrderById method called with id: " + id);
    OrderIdRequest request = OrderIdRequest.newBuilder().setId(id).build();
    OrderResponse response = dataServerStub.getOrderById(request);
    System.out.println("Received response from dataServerStub: " + response);
    return DtoGrpcConverter.OrderResponseGrpc_To_OrderResponseDto(response);
  }

}

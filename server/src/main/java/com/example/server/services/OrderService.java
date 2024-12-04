package com.example.server.services;

import com.example.sep3.grpc.*;
import com.example.server.DataServerStub;
import com.example.server.converters.OfferDtoGrpcConverter;
import com.example.server.converters.OrderDtoGrpcConverter;
import com.example.server.dto.offer.OfferResponseDto;
import com.example.server.dto.order.AddOrderRequestDto;
import com.example.server.dto.order.OrderResponseDto;
import com.example.server.dto.order.PlaceOrderRequestDto;
import com.example.server.dto.order.PlaceOrderSessionResponseDto;
import com.example.shared.model.OrderStatus;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
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
    private final OfferService offerService;
    private final ImageStorageService imageStorageService;

    @Value("${stripe.success-url}")
    private String successUrl; // from application.properties
    @Value("${stripe.cancel-url}")
    private String cancelUrl;
    @Value("${stripe.signing.secret}")
    private String stripeSigningSecret;

    @Autowired
    public OrderService(DataServerStub dataServerStub, OfferService offerService, ImageStorageService imageStorageService) {
        this.dataServerStub = dataServerStub;
        this.offerService = offerService;
        this.imageStorageService = imageStorageService;
        System.out.println("OrderService created");
    }

    @Transactional
    public OrderResponseDto addOrder(
            AddOrderRequestDto orderRequestDto) {
        System.out.println(
                "addOrder method called with request: " + orderRequestDto);
        AddOrderRequest request = OrderDtoGrpcConverter.AddOrderRequestDto_To_AddOrderRequest(
                orderRequestDto);
        System.out.println(
                "Converted AddOrderRequestDto to AddOrderRequest: " + request);
        OrderResponse response = dataServerStub.addOrder(request);
        System.out.println("Received response from dataServerStub: " + response);
        return OrderDtoGrpcConverter.OrderResponseGrpc_To_OrderResponseDto(response);
    }

    public PlaceOrderSessionResponseDto placeOrder(PlaceOrderRequestDto requestDto) {

        AddOrderRequest orderRequest = AddOrderRequest.newBuilder()
                .setOfferId(requestDto.getOfferId())
                .setNumberOfItems(requestDto.getNumberOfItems())
                .setUserId("TestUser123") // not there yet
                .build();

        System.out.println("Request for database built");

        OrderResponse databaseResponse = dataServerStub.addOrder(orderRequest);

        /////////////////////////////////////Temporary////////////////////////////////
        updateOrderStatus(databaseResponse.getId(), OrderStatus.RESERVED.getStatus());
        //////////////////////////////////////////////////////////////////////////////
        if (databaseResponse == null || databaseResponse.getId().isEmpty())
            throw new IllegalArgumentException("Invalid order ID.");

        System.out.println("Order initially saved in database");

    try

    {

        Map<String, Object> sessionParams = new HashMap<>();
        sessionParams.put("payment_method_types", List.of("card"));
        sessionParams.put("mode", "payment");
        sessionParams.put("success_url", successUrl);
        sessionParams.put("cancel_url", cancelUrl);
        sessionParams.put("line_items", List.of(Map.of(
                "price_data", Map.of(
                        "currency", "dkk",
                        "product_data", Map.of("name", "Offer"),
                        "unit_amount", databaseResponse.getPricePerItem() * 100 // Stripe requires amounts in the smallest currency unit
                ),
                "quantity", requestDto.getNumberOfItems()
        )));

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("orderId", databaseResponse.getId());

        sessionParams.put("metadata", metadata);

        Session session = Session.create(sessionParams);

        PlaceOrderSessionResponseDto response = new PlaceOrderSessionResponseDto();
        response.setUrl(session.getUrl());
        response.setSessionId(session.getId());
        return response;

    } catch(
    StripeException e)

    {
        e.printStackTrace();
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

        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject()
                    .orElse(null);

            if (session != null) {
                String orderId = session.getMetadata().get("orderId");

                updateOrderStatus(orderId, OrderStatus.RESERVED.getStatus());

                OrderResponseDto order = getOrderById(orderId);
                updateNumberOfAvailableItems(order.getOfferId(), order.getNumberOfItems());
            }
        }
    }


    public void updateOrderStatus(String orderId, String status)
    {
        OrderStatusRequest updateRequest = OrderStatusRequest.newBuilder()
                .setId(orderId).setStatus(status).build();

        dataServerStub.updateOrderStatus(updateRequest);
    }

    public List<OrderResponseDto> getAllOrders()
    {
        System.out.println("getAllOrders method called");
        EmptyMessage request = EmptyMessage.newBuilder().build();
        OrderList response = dataServerStub.getAllOrders(request);
        System.out.println("Received response from dataServerStub: " + response);
        return response.getOrdersList().stream()
                .map(OrderDtoGrpcConverter::OrderResponseGrpc_To_OrderResponseDto)
                .collect(Collectors.toList());
    }

    public OrderResponseDto getOrderById(String id)
    {
        System.out.println("getOrderById method called with id: " + id);
        OrderIdRequest request = OrderIdRequest.newBuilder().setId(id).build();
        OrderResponse response = dataServerStub.getOrderById(request);
        System.out.println("Received response from dataServerStub: " + response);
        return OrderDtoGrpcConverter.OrderResponseGrpc_To_OrderResponseDto(response);
    }

    private void updateNumberOfAvailableItems(String offerId,
                                              int numberOfItemsToSubtract)
    {
        OfferResponseDto offer = offerService.getOfferById(offerId);
        int oldNumberOfAvailableItems = offer.getNumberOfAvailableItems();
        int newNumberOfAvailableItems =
                oldNumberOfAvailableItems - numberOfItemsToSubtract;
        offer.setNumberOfAvailableItems(newNumberOfAvailableItems);
        offerService.updateOffer(
                OfferDtoGrpcConverter.OfferResponseDto_To_UpdateOfferRequestDto(offer,
                        imageStorageService.extractImage(offer.getImagePath())));
    }

}





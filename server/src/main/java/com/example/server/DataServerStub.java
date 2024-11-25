package com.example.server;

import com.example.sep3.grpc.*;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class DataServerStub {
  private final ManagedChannel channel;
  private final OfferServiceGrpc.OfferServiceBlockingStub blockingStub;
  private final OrderServiceGrpc.OrderServiceBlockingStub orderBlockingStub;

  public DataServerStub(String host, int port) {
    channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext()
        .usePlaintext().build();

    blockingStub = OfferServiceGrpc.newBlockingStub(channel);
    orderBlockingStub = OrderServiceGrpc.newBlockingStub(channel);
    System.out.println("DataServerStub created");
  }

  public OfferList getAvailableOffers(EmptyMessage request) {
    return blockingStub.getAvailableOffers(request);
  }

  public SaveOfferResponse saveOffer(SaveOfferRequest request) {
    return blockingStub.saveOffer(request);
  }

  public OfferResponse getOfferById(OfferIdRequest request) {
    return blockingStub.getOfferById(request);
  }

  public OrderResponse addOrder(AddOrderRequest request) {
    System.out.println("addOrder called with request: " + request);
    OrderResponse response = orderBlockingStub.addOrder(request);
    System.out.println("Received response: " + response);
    return response;
  }

  public OrderResponse getOrderById(OrderIdRequest request) {
    return orderBlockingStub.getOrderById(request);
  }

  public OrderList getAllOrders(EmptyMessage request) {
    return orderBlockingStub.getAllOrders(request);
  }
  public OrderResponse updateOrderStatus(OrderStatusRequest request)
  {
    return orderBlockingStub.updateOrderStatus(request);
  }

  public void shutdown() {
    channel.shutdown();
  }
}

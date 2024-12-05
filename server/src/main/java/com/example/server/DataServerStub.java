package com.example.server;

import com.example.sep3.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class DataServerStub
{
  private final ManagedChannel channel;
  private final OfferServiceGrpc.OfferServiceBlockingStub offerBlockingStub;
  private final OrderServiceGrpc.OrderServiceBlockingStub orderBlockingStub;
  private final BusinessServiceGrpc.BusinessServiceBlockingStub businessBlockingStub;
  private final CustomerServiceGrpc.CustomerServiceBlockingStub customerBlockingStub;
  private final UserServiceGrpc.UserServiceBlockingStub userBlockingStub;

  public DataServerStub(String host, int port)
  {
    channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext()
        .usePlaintext().build();

    offerBlockingStub = OfferServiceGrpc.newBlockingStub(channel);
    orderBlockingStub = OrderServiceGrpc.newBlockingStub(channel);
    businessBlockingStub = BusinessServiceGrpc.newBlockingStub(channel);
    customerBlockingStub = CustomerServiceGrpc.newBlockingStub(channel);
    userBlockingStub = UserServiceGrpc.newBlockingStub(channel);

    System.out.println("DataServerStub created");
  }

  // ********************************* OFFERS *********************************
  public OfferList getAvailableOffers(EmptyMessage request)
  {
    return offerBlockingStub.getAvailableOffers(request);
  }

  public SaveOfferResponse saveOffer(SaveOfferRequest request)
  {
    return offerBlockingStub.saveOffer(request);
  }

  public OfferResponse getOfferById(OfferIdRequest request)
  {
    return offerBlockingStub.getOfferById(request);
  }

  public OfferResponse updateOffer(OfferResponse request)
  {
    return offerBlockingStub.updateOffer(request);
  }

  public FullOfferList getOffers(FilterRequest request)
  {
    return offerBlockingStub.getOffers(request);
  }

  // ********************************* ORDERS *********************************
  public OrderResponse addOrder(AddOrderRequest request)
  {
    System.out.println("addOrder called with request: " + request);
    OrderResponse response = orderBlockingStub.addOrder(request);
    System.out.println("Received response: " + response);
    return response;
  }

  public OrderResponse getOrderById(OrderIdRequest request)
  {
    return orderBlockingStub.getOrderById(request);
  }

  public OrderList getAllOrders(EmptyMessage request)
  {
    return orderBlockingStub.getAllOrders(request);
  }

  public OrderResponse updateOrderStatus(OrderStatusRequest request)
  {
    return orderBlockingStub.updateOrderStatus(request);
  }

  // ********************************* BUSINESS *********************************

  public void registerBusiness(RegisterBusinessRequest request)
  {
    businessBlockingStub.registerBusiness(request);
  }

  public BusinessResponse getBusinessByEmail(BusinessByEmailRequest request)
  {
    return businessBlockingStub.getBusinessByEmail(request);
  }
  public BusinessesInRadiusResponse getBusinessesInRadius(BusinessesInRadiusRequest request)
  {
    return businessBlockingStub.getBusinessesInRadius(request);
  }

  // ******************************** CUSTOMER **************************

  public void registerCustomer(RegisterCustomerRequest request) {customerBlockingStub.registerCustomer(request);}

  public BusinessesInRadiusResponse updateCustomerLocation(CustomerLocationRequest request)
  {
    return customerBlockingStub.updateCustomerLocation(request);
  }
  public CustomerLocationRequest getCustomerLocation(CustomerByEmailRequest request)
  {
    return customerBlockingStub.getCustomerLocation(request);
  }
// ******************* USER *****************

  public UserResponse getUserByEmail(UserByEmailRequest request)
  {
    return userBlockingStub.getUserByEmail(request);
  }



  // ****************************************************************************
  public void shutdown()
  {
    channel.shutdown();
  }
}

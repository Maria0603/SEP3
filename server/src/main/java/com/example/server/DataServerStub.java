package com.example.server;

import com.example.sep3.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class DataServerStub
{
  private final ManagedChannel channel;
  private final OfferServiceGrpc.OfferServiceBlockingStub offerBlockingStub;
  private final PurchaseServiceGrpc.PurchaseServiceBlockingStub purchaseBlockingStub;
  private final BusinessServiceGrpc.BusinessServiceBlockingStub businessBlockingStub;
  private final CustomerServiceGrpc.CustomerServiceBlockingStub customerBlockingStub;
  private final UserServiceGrpc.UserServiceBlockingStub userBlockingStub;

  public DataServerStub(String host, int port)
  {
    channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext()
        .usePlaintext().build();

    offerBlockingStub = OfferServiceGrpc.newBlockingStub(channel);
    purchaseBlockingStub = PurchaseServiceGrpc.newBlockingStub(channel);
    businessBlockingStub = BusinessServiceGrpc.newBlockingStub(channel);
    customerBlockingStub = CustomerServiceGrpc.newBlockingStub(channel);
    userBlockingStub = UserServiceGrpc.newBlockingStub(channel);

    System.out.println("DataServerStub created");
  }

  // ********************************* OFFERS *********************************
  public OfferResponse createOffer(CreateOfferRequest request)
  {
    return offerBlockingStub.createOffer(request);
  }

  public OfferResponse getOfferById(OfferIdRequest request)
  {
    return offerBlockingStub.getOfferById(request);
  }

  public OfferResponse updateOffer(OfferResponse request)
  {
    return offerBlockingStub.updateOffer(request);
  }

  public OfferListResponse getOffers(FilterRequest request)
  {
    return offerBlockingStub.getOffers(request);
  }

  // ********************************* Purchases *********************************
  public PurchaseResponse createPurchase(CreatePurchaseRequest request)
  {
    return purchaseBlockingStub.createPurchase(request);
  }

  public PurchaseResponse getPurchaseById(PurchaseIdRequest request)
  {
    return purchaseBlockingStub.getPurchaseById(request);
  }

  public PurchaseListResponse getPurchases(IdRequestResponse request)
  {
    return purchaseBlockingStub.getPurchases(request);
  }

  public PurchaseResponse updatePurchaseStatus(PurchaseStatusRequest request)
  {
    return purchaseBlockingStub.updatePurchaseStatus(request);
  }

  // ********************************* BUSINESS *********************************

  public IdRequestResponse registerBusiness(RegisterBusinessRequest request)
  {
    return businessBlockingStub.registerBusiness(request);
  }

  public BusinessResponse getBusinessByEmail(EmailRequestResponse request)
  {
    return businessBlockingStub.getBusinessByEmail(request);
  }
  public BusinessesInRadiusResponse getBusinessesInRadius(IdRequestResponse request)
  {
    return businessBlockingStub.getBusinessesInRadius(request);
  }

  // ******************************** CUSTOMER **************************

  public IdRequestResponse registerCustomer(RegisterCustomerRequest request) { return customerBlockingStub.registerCustomer(request);}

  public BusinessesInRadiusResponse updateCustomerLocation(CustomerLocationRequest request)
  {
    return customerBlockingStub.updateCustomerLocation(request);
  }
  public CustomerLocationRequest getCustomerLocation(IdRequestResponse request)
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

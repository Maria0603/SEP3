package com.example.data_server.converters;

import com.example.sep3.grpc.CreatePurchaseRequest;
import com.example.sep3.grpc.DetailedPurchaseResponse;
import com.example.sep3.grpc.PurchaseListResponse;
import com.example.sep3.grpc.PurchaseResponse;
import com.example.shared.converters.DateTimeConverter;
import com.example.shared.entities.domainEntities.Offer;
import com.example.shared.entities.domainEntities.Purchase;
import com.example.shared.entities.usersEntities.Business;
import com.example.shared.entities.usersEntities.Customer;
import com.example.shared.model.PurchaseStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class PurchaseEntityGrpcConverter
{

  public static PurchaseResponse generatePurchaseResponseFromPurchase(
      Purchase purchase)
  {
    return PurchaseResponse.newBuilder().setId(purchase.getId())
        .setCustomerId(purchase.getCustomer().getId())
        .setOfferId(purchase.getOffer().getId())
        .setNumberOfItems(purchase.getNumberOfItems()).setPurchaseTime(
            DateTimeConverter.convertLocalDateTime_To_ProtoTimestamp(
                purchase.getPurchaseTime())).setStatus(purchase.getStatus())
        .setPricePerItem(purchase.getPricePerItem())
        .setBusinessName(purchase.getBusiness().getBusinessName())
        .setBusinessEmail(purchase.getBusiness().getEmail())
        .setCustomerEmail(purchase.getCustomer().getEmail())
        .setBusinessId(purchase.getBusiness().getId()).build();
  }
  public static DetailedPurchaseResponse generateDetailedPurchaseResponseFromPurchase(
      Purchase purchase)
  {
    return DetailedPurchaseResponse.newBuilder().setId(
        purchase.getId())
        .setCustomerId(purchase.getCustomer().getId())
        .setTitle(purchase.getOffer().getTitle())
        .setDescription(purchase.getOffer().getDescription())
        .setNumberOfItems(purchase.getNumberOfItems())
        .setImagePath(purchase.getOffer().getImagePath())
        .setPickupTimeStart(
            DateTimeConverter.convertLocalDateTime_To_ProtoTimestamp(
                purchase.getOffer().getPickupTimeStart()))
        .setPickupTimeEnd(
            DateTimeConverter.convertLocalDateTime_To_ProtoTimestamp(
                purchase.getOffer().getPickupTimeEnd()))
        .setCustomerFirstName(purchase.getCustomer().getFirstName())
        .setCustomerLastName(purchase.getCustomer().getLastName())
        .setCustomerEmail(purchase.getCustomer().getEmail())
        .setCustomerPhoneNumber(purchase.getCustomer().getPhoneNumber())
        .setPurchaseTime(
                DateTimeConverter.convertLocalDateTime_To_ProtoTimestamp(
                purchase.getPurchaseTime()))
        .setCustomerId(purchase.getCustomer().getId())
        .setBusinessId(purchase.getBusiness().getId())
        .setBusinessEmail(purchase.getBusiness().getEmail()).build();
  }

  public static Purchase generatePurchaseFromCreatePurchaseRequest(
      CreatePurchaseRequest request, Offer offer, Customer customer,
      Business business)
  {
    Purchase purchase = new Purchase();

    purchase.setCustomer(customer);
    purchase.setNumberOfItems(request.getNumberOfItems());
    purchase.setPurchaseTime(LocalDateTime.now());
    purchase.setStatus(PurchaseStatus.PENDING.getStatus());

    purchase.setPricePerItem(offer.getOfferPrice());
    purchase.setOffer(offer);

    purchase.setBusiness(business);

    return purchase;
  }

  public static PurchaseListResponse generatePurchaseListResponseFromPurchaseList(
      List<Purchase> purchases) {
    PurchaseListResponse.Builder PurchaseListBuilder = PurchaseListResponse.newBuilder();
    for (Purchase purchase : purchases)
    {
      PurchaseResponse response = generatePurchaseResponseFromPurchase(
          purchase);
      PurchaseListBuilder.addPurchases(response);
    }

    PurchaseListResponse response = PurchaseListBuilder.build();
    return response;
  }
}

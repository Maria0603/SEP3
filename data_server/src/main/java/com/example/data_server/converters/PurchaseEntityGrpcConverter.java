package com.example.data_server.converters;

import com.example.sep3.grpc.CreatePurchaseRequest;
import com.example.sep3.grpc.PurchaseResponse;
import com.example.shared.converters.DateTimeConverter;
import com.example.shared.entities.domainEntities.Offer;
import com.example.shared.entities.domainEntities.Purchase;
import com.example.shared.entities.usersEntities.Business;
import com.example.shared.entities.usersEntities.Customer;
import com.example.shared.model.PurchaseStatus;

import java.time.LocalDateTime;
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
        .setBusinessName(purchase.getBusiness().getBusinessName()).build();
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
}

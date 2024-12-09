package com.example.data_server.converters;

import com.example.sep3.grpc.CreateOfferRequest;
import com.example.sep3.grpc.OfferListResponse;
import com.example.sep3.grpc.OfferResponse;
import com.example.shared.converters.AddressConverter;
import com.example.shared.converters.DateTimeConverter;
import com.example.shared.entities.domainEntities.Offer;
import com.example.shared.entities.usersEntities.Business;
import com.example.shared.model.OfferStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OfferEntityGrpcConverter
{
  public static Offer generateOfferFromCreateOfferRequest(CreateOfferRequest request, Business business)
  {
    Offer offer = new Offer();

    offer.setBusiness(business);

    offer.setTitle(request.getTitle());
    offer.setDescription(request.getDescription());
    offer.setOfferPrice(request.getOfferPrice());
    offer.setOriginalPrice(request.getOriginalPrice());
    offer.setNumberOfAvailableItems(request.getNumberOfAvailableItems());

    offer.setPickupTimeStart(
        DateTimeConverter.convertProtoTimestamp_To_LocalDateTime(
            request.getPickupTimeStart()));
    offer.setPickupTimeEnd(
        DateTimeConverter.convertProtoTimestamp_To_LocalDateTime(
            request.getPickupTimeEnd()));

    offer.setCreationTime(LocalDateTime.now());

    ArrayList<String> categories = new ArrayList<>(request.getCategoriesList());
    offer.setCategories(categories);

    offer.setNumberOfItems(request.getNumberOfItems());
    offer.setStatus(OfferStatus.AVAILABLE.getStatus());
    offer.setImagePath(request.getImagePath());

    return offer;
  }

  public static Offer generateOfferFromOfferResponse(OfferResponse request, Business business)
  {
    Offer offer = new Offer();
    offer.setId(request.getId());
    offer.setTitle(request.getTitle());
    offer.setDescription(request.getDescription());
    offer.setOfferPrice(request.getOfferPrice());
    offer.setOriginalPrice(request.getOriginalPrice());

    offer.setPickupTimeStart(
        DateTimeConverter.convertProtoTimestamp_To_LocalDateTime(
            request.getPickupTimeStart()));
    offer.setPickupTimeEnd(
        DateTimeConverter.convertProtoTimestamp_To_LocalDateTime(
            request.getPickupTimeEnd()));

    ArrayList<String> categories = new ArrayList<>(request.getCategoriesList());
    offer.setCategories(categories);

    offer.setNumberOfItems(request.getNumberOfItems());
    offer.setNumberOfAvailableItems(request.getNumberOfAvailableItems());
    System.out.println("**********************Available: "
        + request.getNumberOfAvailableItems());
    offer.setStatus(request.getStatus());
    offer.setImagePath(request.getImagePath());
    offer.setBusiness(business);

    return offer;
  }

  public static OfferResponse buildOfferResponse(Offer offer)
  {
    return OfferResponse.newBuilder().setId(offer.getId())
        .setTitle(offer.getTitle()).setDescription(offer.getDescription())
        .setStatus(offer.getStatus()).setOfferPrice(offer.getOfferPrice())
        .setOriginalPrice(offer.getOriginalPrice())
        .setNumberOfItems(offer.getNumberOfItems())
        .setNumberOfAvailableItems(offer.getNumberOfAvailableItems())
        .setPickupTimeStart(
            DateTimeConverter.convertLocalDateTime_To_ProtoTimestamp(
                offer.getPickupTimeStart())).setPickupTimeEnd(
            DateTimeConverter.convertLocalDateTime_To_ProtoTimestamp(
                offer.getPickupTimeEnd())).setImagePath(offer.getImagePath())
        .addAllCategories(offer.getCategories())
        .setNumberOfAvailableItems(offer.getNumberOfAvailableItems())
        .setBusinessId(offer.getBusiness().getId())
        .setBusinessName(offer.getBusiness().getBusinessName())
        .setBusinessLogoPath(offer.getBusiness().getLogoPath())
        .setBusinessAddress(AddressConverter.convertAddressToGrpcAddress(
            offer.getBusiness().getAddress())).build();
  }

  public static OfferListResponse buildOfferListResponseFromOffersList(
      List<Offer> offersByCategory)
  {
    OfferListResponse.Builder offerListBuilder = OfferListResponse.newBuilder();
    for (Offer offer : offersByCategory)
    {
      offerListBuilder.addOffer(buildOfferResponse(offer));
    }

    return offerListBuilder.build();
  }
}

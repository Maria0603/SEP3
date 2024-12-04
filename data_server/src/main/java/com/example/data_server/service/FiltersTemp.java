package com.example.data_server.service;

import com.example.data_server.repository.OfferRepository;
import com.example.sep3.grpc.FilterRequest;
import com.example.sep3.grpc.FullOfferList;
import com.example.shared.converters.DateTimeConverter;
import com.example.shared.dao.domainDao.OfferDao;
import io.grpc.stub.StreamObserver;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FiltersTemp {
  private OfferRepository offerRepository;


/*  private void getOffers(FilterRequest request,
      StreamObserver<FullOfferList> responseObserver) {
    List<OfferDao> filteredOffers = new ArrayList<>();

    if (request.hasMinOfferPrice() && request.hasMaxOfferPrice()) {
      filteredOffers = filterOffersByPrice(offerRepository.findAll(), request.getMinOfferPrice(),
          request.getMaxOfferPrice());
    }

    if (request.hasPickupTimeStart() && request.hasPickupTimeEnd()) {
      filteredOffers = filterOffersByTime(filteredOffers,
          DateTimeConverter.convertProtoTimestamp_To_LocalDateTime(request.getPickupTimeStart()), DateTimeConverter.convertProtoTimestamp_To_LocalDateTime(request.getPickupTimeEnd()));
    }

    if (!request.getCategoriesList().isEmpty()) {
      filteredOffers = filterOffersByCategories(filteredOffers, request.getCategoriesList());
    }

    // Build the response
    FullOfferList.Builder offerListBuilder = FullOfferList.newBuilder();
    for (OfferDao offerDao : filteredOffers) {
      offerListBuilder.addOffer(buildOfferResponse(offerDao));
    }


    FullOfferList offerListResponse = offerListBuilder.build();
    logger.info("Sending SaveOfferResponse: {}", offerListResponse);
    responseObserver.onNext(offerListResponse);
    responseObserver.onCompleted();
  }*/


  private List<OfferDao> filterOffersByPrice(List<OfferDao> previousFilterResult, int minPrice, int maxPrice) {
    List<OfferDao> filteredOffers = offerRepository.findByOfferPriceRange(minPrice, maxPrice);
    System.out.println("Offers before filtering by price: ");
    for (OfferDao offer : previousFilterResult) {
      System.out.println("Offer ID: " + offer.getId() + ", Price: " + offer.getOfferPrice());
    }
    System.out.println("Offers before filtering by price: ");
    for (OfferDao offer : filteredOffers) {
      System.out.println("Offer ID: " + offer.getId() + ", Price: " + offer.getOfferPrice());
    }

    List<OfferDao> output = new ArrayList<>();
    System.out.println("Offers after filtering by price: ");
    for (OfferDao offer : filteredOffers) {
      if (previousFilterResult.contains(offer)) {
        output.add(offer);
        System.out.println("Offer ID: " + offer.getId() + ", Price: " + offer.getOfferPrice());
      } else {
        System.out.println("Offer is not in the previous filter result");
      }
    }

    return output;
  }

  private List<OfferDao> filterOffersByTime(List<OfferDao> previousFilterResult, LocalDateTime pickupTimeStart,
      LocalDateTime pickupTimeEnd) {
    System.out.println("Filtering by time");
    List<OfferDao> filteredOffers = offerRepository.findByPickupTimeRange(
        pickupTimeStart, pickupTimeEnd);
    for (OfferDao offer : previousFilterResult) {
      System.out.println("Offer ID: " + offer.getId() + ", Price: " + offer.getOfferPrice());
    }
    List<OfferDao> output = new ArrayList<>();
    for (OfferDao offer : filteredOffers) {
      if (previousFilterResult.contains(offer)) {
        output.add(offer);
      }
    }
    for (OfferDao offer : previousFilterResult) {
      System.out.println("Offer ID: " + offer.getId() + ", Price: " + offer.getOfferPrice());
    }
    return output;
  }

  private List<OfferDao> filterOffersByCategories(List<OfferDao> previousFilterResult, List<String> categories) {
    List<OfferDao> filteredOffers = offerRepository.findByCategories(
        categories);
    for (OfferDao offer : previousFilterResult) {
      System.out.println("Offer ID: " + offer.getId() + ", Price: " + offer.getOfferPrice());
    }
    List<OfferDao> output = new ArrayList<>();
    for (OfferDao offer : filteredOffers) {
      if (previousFilterResult.contains(offer)) {
        output.add(offer);
      }
    }
    for (OfferDao offer : previousFilterResult) {
      System.out.println("Offer ID: " + offer.getId() + ", Price: " + offer.getOfferPrice());
    }
    return output;
  }
}

package com.example.data_server.service;

import com.example.data_server.repository.OfferRepository;
import com.example.shared.entities.domainEntities.Offer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FiltersTemp {
  private OfferRepository offerRepository;





  private List<Offer> filterOffersByPrice(List<Offer> previousFilterResult, int minPrice, int maxPrice) {
    List<Offer> filteredOffers = offerRepository.findByOfferPriceRange(minPrice, maxPrice);
    System.out.println("Offers before filtering by price: ");
    for (Offer offer : previousFilterResult) {
      System.out.println("Offer ID: " + offer.getId() + ", Price: " + offer.getOfferPrice());
    }
    System.out.println("Offers before filtering by price: ");
    for (Offer offer : filteredOffers) {
      System.out.println("Offer ID: " + offer.getId() + ", Price: " + offer.getOfferPrice());
    }

    List<Offer> output = new ArrayList<>();
    System.out.println("Offers after filtering by price: ");
    for (Offer offer : filteredOffers) {
      if (previousFilterResult.contains(offer)) {
        output.add(offer);
        System.out.println("Offer ID: " + offer.getId() + ", Price: " + offer.getOfferPrice());
      } else {
        System.out.println("Offer is not in the previous filter result");
      }
    }

    return output;
  }

  private List<Offer> filterOffersByTime(List<Offer> previousFilterResult, LocalDateTime pickupTimeStart,
      LocalDateTime pickupTimeEnd) {
    System.out.println("Filtering by time");
    List<Offer> filteredOffers = offerRepository.findByPickupTimeRange(
        pickupTimeStart, pickupTimeEnd);
    for (Offer offer : previousFilterResult) {
      System.out.println("Offer ID: " + offer.getId() + ", Price: " + offer.getOfferPrice());
    }
    List<Offer> output = new ArrayList<>();
    for (Offer offer : filteredOffers) {
      if (previousFilterResult.contains(offer)) {
        output.add(offer);
      }
    }
    for (Offer offer : previousFilterResult) {
      System.out.println("Offer ID: " + offer.getId() + ", Price: " + offer.getOfferPrice());
    }
    return output;
  }

  private List<Offer> filterOffersByCategories(List<Offer> previousFilterResult, List<String> categories) {
    List<Offer> filteredOffers = offerRepository.findByCategories(
        categories);
    for (Offer offer : previousFilterResult) {
      System.out.println("Offer ID: " + offer.getId() + ", Price: " + offer.getOfferPrice());
    }
    List<Offer> output = new ArrayList<>();
    for (Offer offer : filteredOffers) {
      if (previousFilterResult.contains(offer)) {
        output.add(offer);
      }
    }
    for (Offer offer : previousFilterResult) {
      System.out.println("Offer ID: " + offer.getId() + ", Price: " + offer.getOfferPrice());
    }
    return output;
  }
}

package com.example.data_server.service;

import com.example.data_server.repository.OfferRepository;
import com.example.data_server.utility.DateTimeConverter;
import com.example.sep3.grpc.*;
import com.example.shared.dao.OfferDao;
import com.example.shared.dao.TimeDao;
import com.example.shared.model.OfferStatus;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@GrpcService public class OfferServiceImpl
    extends OfferServiceGrpc.OfferServiceImplBase {
  private OfferRepository offerRepository;

  @Autowired public OfferServiceImpl(OfferRepository offerRepository) {
    this.offerRepository = offerRepository;
    System.out.println("OfferServiceImpl created");

  }

  @Override public void saveOffer(SaveOfferRequest request,
      StreamObserver<SaveOfferResponse> responseObserver) {
    System.out.println("Request for save offer");

    // Prepare to save the offer in database
    OfferDao offer = generateOfferDaoFromSaveOfferRequest(request);

    // Save the offer
    offerRepository.save(offer);

    // Build the response with everything
    SaveOfferResponse response = buildSaveOfferResponse(offer);

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override public void getAvailableOffers(EmptyMessage request,
      StreamObserver<OfferList> responseObserver) {
    System.out.println("Request for all offers");
    List<OfferDao> availableOffers = offerRepository.findByStatus(
        OfferStatus.AVAILABLE.getStatus());

    OfferList.Builder offerListBuilder = OfferList.newBuilder();
    for (OfferDao offerDao : availableOffers)
      offerListBuilder.addOffer(
          buildShortOfferResponse(offerDao)); //method to build the response

    OfferList offerListResponse = offerListBuilder.build();
    responseObserver.onNext(offerListResponse);
    responseObserver.onCompleted();
  }

  @Override public void getOffers(FilterRequest request,
      StreamObserver<OfferList> responseObserver) {

    List<OfferDao> filteredOffers = new ArrayList<>();

    if (request.hasMinOfferPrice() && request.hasMaxOfferPrice()) {
      filteredOffers = filterOffersByPrice(offerRepository.findAll(), request.getMinOfferPrice(),
          request.getMaxOfferPrice());
    }

    if (request.hasPickupTimeStart() && request.hasPickupTimeEnd()) {
      filteredOffers = filterOffersByTime(filteredOffers, DateTimeConverter.convertGrpcTimeToTimeDao(request.getPickupTimeStart()),
          DateTimeConverter.convertGrpcTimeToTimeDao(request.getPickupTimeEnd()));
    }

    if (!request.getCategoriesList().isEmpty()) {
      filteredOffers = filterOffersByCategories(filteredOffers, request.getCategoriesList());
    }

    // Build the response
    OfferList.Builder offerListBuilder = OfferList.newBuilder();
    for (OfferDao offerDao : filteredOffers) {
      offerListBuilder.addOffer(buildShortOfferResponse(offerDao));
    }

    OfferList offerListResponse = offerListBuilder.build();
    responseObserver.onNext(offerListResponse);
    responseObserver.onCompleted();
  }

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
      }else {
        System.out.println("Offer is not in the previous filter result");
      }
    }

    return output;
  }


  private List<OfferDao> filterOffersByTime(List<OfferDao> previousFilterResult, TimeDao pickupTimeStart,
      TimeDao pickupTimeEnd) {
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


  @Override public void getOfferById(OfferIdRequest request,
      StreamObserver<OfferResponse> responseObserver) {
    System.out.println("Request for offer by id");

    Optional<OfferDao> offer = offerRepository.findById(request.getId());
    if (offer.isPresent()) {
      OfferDao offerDao = offer.get();

      OfferResponse offerResponse = buildOfferResponse(
          offerDao); //method to build the response

      responseObserver.onNext(offerResponse);
      responseObserver.onCompleted();
    }

  }

  @Override public void updateOffer(OfferResponse request,
      StreamObserver<OfferResponse> responseObserver) {
    System.out.println("Request for update offer.");
    if (!offerRepository.existsById(request.getId()))
      throw new IllegalArgumentException(
          "Offer with ID " + request.getId() + " not found.");

    // Save the updated document (will replace the existing one)
    OfferDao updatedOffer = offerRepository.save(
        generateOfferDaoFromOfferResponse(request));
    OfferResponse offerResponse = buildOfferResponse(
        updatedOffer); //method to build the response

    responseObserver.onNext(offerResponse);
    responseObserver.onCompleted();

  }

  private OfferDao generateOfferDaoFromSaveOfferRequest(
      SaveOfferRequest request) {
    OfferDao offer = new OfferDao();
    offer.setTitle(request.getTitle());
    offer.setDescription(request.getDescription());
    offer.setOfferPrice(request.getOfferPrice());
    offer.setOriginalPrice(request.getOriginalPrice());
    offer.setNumberOfAvailableItems(request.getNumberOfAvailableItems());

    offer.setPickupDate(
        DateTimeConverter.convertGrpcDateToDateDao(request.getPickupDate()));
    offer.setPickupTimeStart(DateTimeConverter.convertGrpcTimeToTimeDao(
        request.getPickupTimeStart()));
    offer.setPickupTimeEnd(
        DateTimeConverter.convertGrpcTimeToTimeDao(request.getPickupTimeEnd()));

    ArrayList<String> categories = new ArrayList<>(request.getCategoriesList());
    offer.setCategories(categories);

    offer.setNumberOfItems(request.getNumberOfItems());
    offer.setStatus(OfferStatus.AVAILABLE.getStatus());
    offer.setImagePath(request.getImagePath());

    return offer;
  }

  private OfferDao generateOfferDaoFromOfferResponse(OfferResponse request) {
    OfferDao offer = new OfferDao();
    offer.setId(request.getId());
    offer.setTitle(request.getTitle());
    offer.setDescription(request.getDescription());
    offer.setOfferPrice(request.getOfferPrice());
    offer.setOriginalPrice(request.getOriginalPrice());

    offer.setPickupDate(
        DateTimeConverter.convertGrpcDateToDateDao(request.getPickupDate()));
    offer.setPickupTimeStart(DateTimeConverter.convertGrpcTimeToTimeDao(
        request.getPickupTimeStart()));
    offer.setPickupTimeEnd(
        DateTimeConverter.convertGrpcTimeToTimeDao(request.getPickupTimeEnd()));

    ArrayList<String> categories = new ArrayList<>(request.getCategoriesList());
    offer.setCategories(categories);

    offer.setNumberOfItems(request.getNumberOfItems());
    offer.setNumberOfAvailableItems(request.getNumberOfAvailableItems());
    System.out.println("**********************Available: "
        + request.getNumberOfAvailableItems());
    offer.setStatus(request.getStatus());
    offer.setImagePath(request.getImagePath());

    return offer;
  }

  private SaveOfferResponse buildSaveOfferResponse(OfferDao offer) {
    return SaveOfferResponse.newBuilder().setId(offer.getId())
        .setTitle(offer.getTitle()).setDescription(offer.getDescription())
        .setOfferPrice(offer.getOfferPrice())
        .setOriginalPrice(offer.getOriginalPrice())
        .setNumberOfItems(offer.getNumberOfItems()).setPickupDate(
            DateTimeConverter.convertDateDaoToGrpcDate(offer.getPickupDate()))
        .setPickupTimeStart(DateTimeConverter.convertTimeDaoToGrpcTime(
            offer.getPickupTimeStart())).setPickupTimeEnd(
            DateTimeConverter.convertTimeDaoToGrpcTime(
                offer.getPickupTimeEnd())).setImagePath(offer.getImagePath())
        .addAllCategories(offer.getCategories()).build();

  }

  private OfferResponse buildOfferResponse(OfferDao offerDao) {
    return OfferResponse.newBuilder().setId(offerDao.getId())
        .setTitle(offerDao.getTitle()).setDescription(offerDao.getDescription())
        .setStatus(offerDao.getStatus()).setOfferPrice(offerDao.getOfferPrice())
        .setOriginalPrice(offerDao.getOriginalPrice())
        .setNumberOfItems(offerDao.getNumberOfItems())
        .setNumberOfAvailableItems(offerDao.getNumberOfAvailableItems())
        .setPickupDate(DateTimeConverter.convertDateDaoToGrpcDate(
            offerDao.getPickupDate())).setPickupTimeStart(
            DateTimeConverter.convertTimeDaoToGrpcTime(
                offerDao.getPickupTimeStart())).setPickupTimeEnd(
            DateTimeConverter.convertTimeDaoToGrpcTime(
                offerDao.getPickupTimeEnd()))
        .setImagePath(offerDao.getImagePath())
        .addAllCategories(offerDao.getCategories())
        .setNumberOfAvailableItems(offerDao.getNumberOfAvailableItems())
        .build();
  }

  private ShortOfferResponse buildShortOfferResponse(OfferDao offerDao) {
    return ShortOfferResponse.newBuilder().setId(offerDao.getId())
        .setTitle(offerDao.getTitle()).setStatus(offerDao.getStatus())
        .setOfferPrice(offerDao.getOfferPrice())
        .setOriginalPrice(offerDao.getOriginalPrice())
        .setNumberOfAvailableItems(offerDao.getNumberOfAvailableItems())
        .setPickupDate(DateTimeConverter.convertDateDaoToGrpcDate(
            offerDao.getPickupDate())).setPickupTimeStart(
            DateTimeConverter.convertTimeDaoToGrpcTime(
                offerDao.getPickupTimeStart())).setPickupTimeEnd(
            DateTimeConverter.convertTimeDaoToGrpcTime(
                offerDao.getPickupTimeEnd()))
        .setImagePath(offerDao.getImagePath()).build();
  }

}




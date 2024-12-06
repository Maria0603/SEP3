package com.example.data_server.service;

import com.example.data_server.repository.BusinessRepository;
import com.example.data_server.repository.CustomerRepository;
import com.example.data_server.repository.OfferRepository;
import com.example.data_server.utility.GeoUtils;
import com.example.sep3.grpc.*;
import com.example.shared.converters.DateTimeConverter;
import com.example.shared.dao.domainDao.OfferDao;
import com.example.shared.dao.usersDao.BusinessDao;
import com.example.shared.dao.usersDao.CustomerDao;
import com.example.shared.model.OfferStatus;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@GrpcService public class OfferServiceImpl
    extends OfferServiceGrpc.OfferServiceImplBase
{
  private final BusinessRepository businessRepository;
  private OfferRepository offerRepository;
  private CustomerRepository customerRepository;

  private static final Logger logger = LoggerFactory.getLogger(
      OfferServiceImpl.class);

  @Autowired public OfferServiceImpl(OfferRepository offerRepository,
      CustomerRepository customerRepository,
      BusinessRepository businessRepository)
  {
    this.offerRepository = offerRepository;
    this.customerRepository = customerRepository;
    this.businessRepository = businessRepository;
    System.out.println("OfferServiceImpl created");
  }

  @Override public void saveOffer(SaveOfferRequest request,
      StreamObserver<SaveOfferResponse> responseObserver)
  {
    System.out.println("Request for save offer");

    // Prepare to save the offer in database
    OfferDao offer = generateOfferDaoFromSaveOfferRequest(request);

    // Save the offer
    OfferDao createdOffer = offerRepository.save(offer);

    // Build the response with everything
    SaveOfferResponse response = buildSaveOfferResponse(createdOffer);

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override public void getOfferById(OfferIdRequest request,
      StreamObserver<OfferResponse> responseObserver)
  {
    System.out.println("Request for offer by id");

    Optional<OfferDao> offer = offerRepository.findById(request.getId());
    if (offer.isPresent())
    {
      OfferDao offerDao = offer.get();

      OfferResponse offerResponse = buildOfferResponse(
          offerDao); // method to build the response

      responseObserver.onNext(offerResponse);
      responseObserver.onCompleted();
    }

  }

  @Override public void updateOffer(OfferResponse request,
      StreamObserver<OfferResponse> responseObserver)
  {
    System.out.println("Request for update offer.");
    if (!offerRepository.existsById(request.getId()))
      throw new IllegalArgumentException(
          "Offer with ID " + request.getId() + " not found.");

    // Save the updated document (will replace the existing one)
    OfferDao updatedOffer = offerRepository.save(
        generateOfferDaoFromOfferResponse(request));
    OfferResponse offerResponse = buildOfferResponse(
        updatedOffer); // method to build the response

    responseObserver.onNext(offerResponse);
    responseObserver.onCompleted();

  }

  @Override public void getOffersByCategory(CategoryRequest request,
      StreamObserver<FullOfferList> responseObserver)
  {
    logger.info("Request for offers by category: {}", request);

    List<OfferDao> offersByCategory = offerRepository.findByCategories(
        request.getCategoriesList());

    buildFullOfferListResponseFromListDao(responseObserver, offersByCategory);
  }

  @Override public void getOffersByPriceRange(PriceRangeRequest request,
      StreamObserver<FullOfferList> responseObserver)
  {
    logger.info("Request for offers by price range: {} - {}",
        request.getMinOfferPrice(), request.getMaxOfferPrice());

    List<OfferDao> offersByPriceRange = offerRepository.findByOfferPriceRange(
        request.getMinOfferPrice(), request.getMaxOfferPrice());

    buildFullOfferListResponseFromListDao(responseObserver, offersByPriceRange);
  }

  @Override public void getOffersByTime(TimeRangeRequest request,
      StreamObserver<FullOfferList> responseObserver)
  {
    logger.info("Request for offers by time range: {} - {}", request.getStart(),
        request.getEnd());

    LocalDateTime startTime = DateTimeConverter.convertProtoTimestamp_To_LocalDateTime(
        request.getStart());
    LocalDateTime endTime = DateTimeConverter.convertProtoTimestamp_To_LocalDateTime(
        request.getEnd());

    List<OfferDao> offersByTimeRange = offerRepository.findByPickupTimeRange(
        startTime, endTime);

    FullOfferList.Builder offerListBuilder = FullOfferList.newBuilder();
    for (OfferDao offerDao : offersByTimeRange)
    {
      offerListBuilder.addOffer(buildOfferResponse(offerDao));
    }

    FullOfferList offerListResponse = offerListBuilder.build();
    logger.info("Sending FullOfferList response: {}", offerListResponse);
    responseObserver.onNext(offerListResponse);
    responseObserver.onCompleted();
  }

  @Override public void getOffers(FilterRequest request,
      StreamObserver<FullOfferList> responseObserver)
  {

    Optional<CustomerDao> customer = customerRepository.findById(
        request.getUserId());

    CustomerDao customerDao;
    boolean hasLocationFilter;
    if (customer.isPresent())
    {
      customerDao = customer.get();
      hasLocationFilter =
          customerDao.getLatitude() != 0 && customerDao.getLongitude() != 0
              && customerDao.getSearchRadius() > 0;
    }
    else
    {
      customerDao = null;
      hasLocationFilter = false;
    }

    FullOfferList offerListResponse = offerListBuilder.build();
    logger.info("Sending SaveOfferResponse: {}", offerListResponse);
    responseObserver.onNext(offerListResponse);
    responseObserver.onCompleted();
  }

  @Override public void getOffers(FilterRequest request,
      StreamObserver<FullOfferList> responseObserver) {
    try{
    List<OfferDao> filteredOffers;
    logger.info("Received FilterRequest: {}", request);

    var allOffers = offerRepository.findAll();
    logger.info("Received Offers: ", allOffers);
    filteredOffers = allOffers.stream().filter(
            item -> !request.hasMaxOfferPrice()
                || item.getOfferPrice() <= request.getMaxOfferPrice()).filter(
            item -> !request.hasMinOfferPrice()
                || item.getOfferPrice() >= request.getMinOfferPrice()).filter(
            item -> !request.hasPickupTimeStart() || !item.getPickupTimeStart()
                .isBefore(DateTimeConverter.convertProtoTimestamp_To_LocalDateTime(
                    request.getPickupTimeStart()))).filter(
            item -> !request.hasPickupTimeEnd() || item.getPickupTimeEnd().isAfter(
                DateTimeConverter.convertProtoTimestamp_To_LocalDateTime(
                    request.getPickupTimeEnd()))).filter(
            item -> request.getCategoriesList().isEmpty() || item.getCategories()
                .stream().anyMatch(request.getCategoriesList()::contains))
        .filter(item -> {
          if (hasLocationFilter)
          {
            BusinessDao business = item.getBusiness();
            double distance = GeoUtils.calculateDistance(
                customerDao.getLatitude(), customerDao.getLongitude(),
                business.getLocation().getCoordinates().getLast(),
                business.getLocation().getCoordinates().getFirst());
            return distance <= customerDao.getSearchRadius();
          }
          return true; // If no location filter, include all offers
        })//.sorted((o1, o2) -> o2.getCreationTime().compareTo(o1.getCreationTime()))
        .toList();

    filteredOffers = filteredOffers.stream().sorted(
            (o1, o2) -> o2.getCreationTime().compareTo(o1.getCreationTime()))
        .toList();
    //    var test = offerRepository.findAll();
    //    filteredOffers = test.stream().filter(
    //        offer -> offer.getCategories().stream()
    //            .anyMatch(request.getCategoriesList()::contains)).toList();

    // Build the response
    buildFullOfferListResponseFromListDao(responseObserver, filteredOffers);
}

    FullOfferList offerListResponse = offerListBuilder.build();
    logger.info("Sending SaveOfferResponse: {}", offerListResponse);
    responseObserver.onNext(offerListResponse);
    responseObserver.onCompleted();
  } catch (Exception e) {
    logger.error("Error processing getOffers: ", e);
    responseObserver.onError(io.grpc.Status.INTERNAL.withDescription("Internal server error").withCause(e).asRuntimeException());
  }
private OfferDao generateOfferDaoFromSaveOfferRequest(SaveOfferRequest request)
{
  OfferDao offer = new OfferDao();
  Optional<BusinessDao> businessOptional = businessRepository.findById(
      request.getBusinessId());
  if (businessOptional.isEmpty())
    throw new IllegalArgumentException(
        "Business not found with ID: " + request.getBusinessId());

  BusinessDao business = businessOptional.get();
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

private OfferDao generateOfferDaoFromOfferResponse(OfferResponse request)
{
  OfferDao offer = new OfferDao();
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

  return offer;
}

private SaveOfferResponse buildSaveOfferResponse(OfferDao offer)
{
  return SaveOfferResponse.newBuilder().setId(offer.getId())
      .setTitle(offer.getTitle()).setDescription(offer.getDescription())
      .setOfferPrice(offer.getOfferPrice())
      .setOriginalPrice(offer.getOriginalPrice())
      .setNumberOfItems(offer.getNumberOfItems()).setPickupTimeStart(
          DateTimeConverter.convertLocalDateTime_To_ProtoTimestamp(
              offer.getPickupTimeStart())).setPickupTimeEnd(
          DateTimeConverter.convertLocalDateTime_To_ProtoTimestamp(
              offer.getPickupTimeEnd())).setImagePath(offer.getImagePath())
      .addAllCategories(offer.getCategories())
      .setBusinessId(offer.getBusiness().getId())
      .setBusinessName(offer.getBusiness().getBusinessName())
      .setBusinessLogoPath(offer.getBusiness().getLogoPath()).build();
}

private OfferResponse buildOfferResponse(OfferDao offerDao)
{
  return OfferResponse.newBuilder().setId(offerDao.getId())
      .setTitle(offerDao.getTitle()).setDescription(offerDao.getDescription())
      .setStatus(offerDao.getStatus()).setOfferPrice(offerDao.getOfferPrice())
      .setOriginalPrice(offerDao.getOriginalPrice())
      .setNumberOfItems(offerDao.getNumberOfItems())
      .setNumberOfAvailableItems(offerDao.getNumberOfAvailableItems())
      .setPickupTimeStart(
          DateTimeConverter.convertLocalDateTime_To_ProtoTimestamp(
              offerDao.getPickupTimeStart())).setPickupTimeEnd(
          DateTimeConverter.convertLocalDateTime_To_ProtoTimestamp(
              offerDao.getPickupTimeEnd()))
      .setImagePath(offerDao.getImagePath())
      .addAllCategories(offerDao.getCategories())
      .setNumberOfAvailableItems(offerDao.getNumberOfAvailableItems())
      .setBusinessId(offerDao.getBusiness().getId())
      .setBusinessName(offerDao.getBusiness().getBusinessName())
      .setBusinessLogoPath(offerDao.getBusiness().getLogoPath()).build();
}

private void buildFullOfferListResponseFromListDao(
    StreamObserver<FullOfferList> responseObserver,
    List<OfferDao> offersByCategory)
{
  FullOfferList.Builder offerListBuilder = FullOfferList.newBuilder();
  for (OfferDao offerDao : offersByCategory)
  {
    offerListBuilder.addOffer(buildOfferResponse(offerDao));
  }

  FullOfferList offerListResponse = offerListBuilder.build();
  logger.info("Sending SaveOfferResponse: {}", offerListResponse);
  responseObserver.onNext(offerListResponse);
  responseObserver.onCompleted();
}
}

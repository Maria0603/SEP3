package com.example.data_server.service;

import com.example.data_server.repository.BusinessRepository;
import com.example.data_server.repository.CustomerRepository;
import com.example.data_server.repository.OfferRepository;
import com.example.data_server.utility.GeoUtils;
import com.example.sep3.grpc.*;
import com.example.shared.converters.AddressConverter;
import com.example.shared.converters.DateTimeConverter;
import com.example.shared.entities.domainEntities.Offer;
import com.example.shared.entities.usersEntities.Business;
import com.example.shared.entities.usersEntities.Customer;
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

  @Override public void createOffer(CreateOfferRequest request,
      StreamObserver<OfferResponse> responseObserver)
  {
    System.out.println("Request for save offer");

    // Prepare to save the offer in database
    Offer offer = generateOfferFromCreateOfferRequest(request);

    // Save the offer
    Offer createdOffer = offerRepository.save(offer);

    // Build the response with everything
    OfferResponse response = buildOfferResponse(createdOffer);

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override public void getOfferById(OfferIdRequest request,
      StreamObserver<OfferResponse> responseObserver)
  {
    System.out.println("Request for offer by id");

    Optional<Offer> offer = offerRepository.findById(request.getId());
    if (offer.isPresent())
    {
      Offer offerDao = offer.get();

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
    Offer updatedOffer = offerRepository.save(
        generateOfferDaoFromOfferResponse(request));
    OfferResponse offerResponse = buildOfferResponse(
        updatedOffer); // method to build the response

    responseObserver.onNext(offerResponse);
    responseObserver.onCompleted();

  }

  @Override public void getOffersByCategory(CategoryRequest request,
      StreamObserver<OfferListResponse> responseObserver)
  {
    logger.info("Request for offers by category: {}", request);

    List<Offer> offersByCategory = offerRepository.findByCategories(
        request.getCategoriesList());

    buildOfferListResponseFromOffersList(responseObserver, offersByCategory);
  }

  @Override public void getOffersByPriceRange(PriceRangeRequest request,
      StreamObserver<OfferListResponse> responseObserver)
  {
    logger.info("Request for offers by price range: {} - {}",
        request.getMinOfferPrice(), request.getMaxOfferPrice());

    List<Offer> offersByPriceRange = offerRepository.findByOfferPriceRange(
        request.getMinOfferPrice(), request.getMaxOfferPrice());

    buildOfferListResponseFromOffersList(responseObserver, offersByPriceRange);
  }

  @Override public void getOffersByTime(TimeRangeRequest request,
      StreamObserver<OfferListResponse> responseObserver)
  {
    logger.info("Request for offers by time range: {} - {}", request.getStart(),
        request.getEnd());

    LocalDateTime startTime = DateTimeConverter.convertProtoTimestamp_To_LocalDateTime(
        request.getStart());
    LocalDateTime endTime = DateTimeConverter.convertProtoTimestamp_To_LocalDateTime(
        request.getEnd());

    List<Offer> offersByTimeRange = offerRepository.findByPickupTimeRange(
        startTime, endTime);

    OfferListResponse.Builder offerListBuilder = OfferListResponse.newBuilder();
    for (Offer offer : offersByTimeRange)
    {
      offerListBuilder.addOffer(buildOfferResponse(offer));
    }

    OfferListResponse offerListResponse = offerListBuilder.build();
    logger.info("Sending FullOfferList response: {}", offerListResponse);
    responseObserver.onNext(offerListResponse);
    responseObserver.onCompleted();
  }

  @Override public void getOffers(FilterRequest request,
      StreamObserver<OfferListResponse> responseObserver)
  {

    Optional<Customer> customer = customerRepository.findById(
        request.getUserId());

    // Extract the radius from the customer, if they have one
    Customer customerDao;
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
      //If the user is not a customer, it must be business, and they don't have a radius
      customerDao = null;
      hasLocationFilter = false;
    }

    List<Offer> filteredOffers;
    var allAvailableOffers = offerRepository.findByStatus(OfferStatus.AVAILABLE.getStatus());
    filteredOffers = allAvailableOffers.stream().filter(
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
            Business business = item.getBusiness();
            double distance = GeoUtils.calculateDistance(
                customerDao.getLatitude(), customerDao.getLongitude(),
                business.getLocation().getCoordinates().getLast(),
                business.getLocation().getCoordinates().getFirst());
            System.out.println("Business: " + business.getId());
            System.out.println("Customer: " + customerDao.getId() + " radius: "
                + customer.get().getSearchRadius());

            System.out.println(
                "Distance: " + distance + " for ID " + item.getId());
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
    buildOfferListResponseFromOffersList(responseObserver, filteredOffers);
  }

  private Offer generateOfferFromCreateOfferRequest(
      CreateOfferRequest request)
  {
    Offer offer = new Offer();
    Optional<Business> businessOptional = businessRepository.findById(
        request.getBusinessId());
    if (businessOptional.isEmpty())
      throw new IllegalArgumentException(
          "Business not found with ID: " + request.getBusinessId());

    Business business = businessOptional.get();
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

  private Offer generateOfferDaoFromOfferResponse(OfferResponse request)
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
    offer.setBusiness(
        businessRepository.findById(request.getBusinessId()).get());

    return offer;
  }

  private OfferResponse buildOfferResponse(Offer offer)
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
                offer.getPickupTimeEnd()))
        .setImagePath(offer.getImagePath())
        .addAllCategories(offer.getCategories())
        .setNumberOfAvailableItems(offer.getNumberOfAvailableItems())
        .setBusinessId(offer.getBusiness().getId())
        .setBusinessName(offer.getBusiness().getBusinessName())
        .setBusinessLogoPath(offer.getBusiness().getLogoPath())
        .setBusinessAddress(AddressConverter.convertAddressDaoToGrpcAddress(
            offer.getBusiness().getAddress())).build();
  }

  private void buildOfferListResponseFromOffersList(
      StreamObserver<OfferListResponse> responseObserver,
      List<Offer> offersByCategory)
  {
    OfferListResponse.Builder offerListBuilder = OfferListResponse.newBuilder();
    for (Offer offer : offersByCategory)
    {
      offerListBuilder.addOffer(buildOfferResponse(offer));
    }

    OfferListResponse offerListResponse = offerListBuilder.build();
    logger.info("Sending SaveOfferResponse: {}", offerListResponse);
    responseObserver.onNext(offerListResponse);
    responseObserver.onCompleted();
  }
}

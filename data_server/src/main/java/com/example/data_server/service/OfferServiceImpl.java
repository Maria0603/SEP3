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

import static converters.OfferEntityGrpcConverter.*;

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

    Optional<Business> businessOptional = businessRepository.findById(
        request.getBusinessId());
    if (businessOptional.isEmpty())
      throw new IllegalArgumentException(
          "Business not found with ID: " + request.getBusinessId());

    Business business = businessOptional.get();

    // Prepare to save the offer in database
    Offer offer = generateOfferFromCreateOfferRequest(request, business);

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

    Optional<Offer> offerOptional = offerRepository.findById(request.getId());
    if (offerOptional.isPresent())
    {
      Offer offer = offerOptional.get();

      OfferResponse offerResponse = buildOfferResponse(
          offer); // method to build the response

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

    Business business = businessRepository.findById(request.getBusinessId()).get();

    // Save the updated document (will replace the existing one)
    Offer updatedOffer = offerRepository.save(
        generateOfferFromOfferResponse(request, business));
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

    responseObserver.onNext(
        buildOfferListResponseFromOffersList(offersByCategory));
    responseObserver.onCompleted();
  }

  @Override public void getOffersByPriceRange(PriceRangeRequest request,
      StreamObserver<OfferListResponse> responseObserver)
  {
    logger.info("Request for offers by price range: {} - {}",
        request.getMinOfferPrice(), request.getMaxOfferPrice());

    List<Offer> offersByPriceRange = offerRepository.findByOfferPriceRange(
        request.getMinOfferPrice(), request.getMaxOfferPrice());

    responseObserver.onNext(
        buildOfferListResponseFromOffersList(offersByPriceRange));
    responseObserver.onCompleted();
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

    Optional<Customer> customerOptional = customerRepository.findById(
        request.getUserId());

    // Extract the radius from the customer, if they have one
    Customer customer;
    boolean hasLocationFilter;
    if (customerOptional.isPresent())
    {
      customer = customerOptional.get();
      hasLocationFilter =
          customer.getLatitude() != 0 && customer.getLongitude() != 0
              && customer.getSearchRadius() > 0;
    }
    else
    {
      //If the user is not a customer, it must be business, and they don't have a radius
      customer = null;
      hasLocationFilter = false;
    }

    List<Offer> filteredOffers;
    var allAvailableOffers = offerRepository.findByStatus(
        OfferStatus.AVAILABLE.getStatus());
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
            double distance = GeoUtils.calculateDistance(customer.getLatitude(),
                customer.getLongitude(),
                business.getLocation().getCoordinates().getLast(),
                business.getLocation().getCoordinates().getFirst());
            System.out.println("Business: " + business.getId());
            System.out.println("Customer: " + customer.getId() + " radius: "
                + customerOptional.get().getSearchRadius());

            System.out.println(
                "Distance: " + distance + " for ID " + item.getId());
            return distance <= customer.getSearchRadius();
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
    OfferListResponse offers = buildOfferListResponseFromOffersList(filteredOffers);
    logger.info("Sending SaveOfferResponse: {}", offers);

    responseObserver.onNext(offers);
    responseObserver.onCompleted();
  }

}

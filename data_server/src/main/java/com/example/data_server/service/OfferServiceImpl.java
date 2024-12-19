package com.example.data_server.service;

import com.example.data_server.repository.BusinessRepository;
import com.example.data_server.repository.OfferRepository;
import com.example.sep3.grpc.*;
import com.example.shared.converters.DateTimeConverter;
import com.example.shared.entities.domainEntities.Offer;
import com.example.shared.entities.userEntities.Business;
import com.example.shared.model.OfferStatus;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.data_server.converters.OfferEntityGrpcConverter.*;
import static com.example.data_server.converters.OfferEntityGrpcConverter.buildOfferListResponseFromOffersList;

@GrpcService public class OfferServiceImpl
    extends OfferServiceGrpc.OfferServiceImplBase
{
  private final BusinessRepository businessRepository;
  private OfferRepository offerRepository;
  private final MongoTemplate mongoTemplate;

  private static final Logger logger = LoggerFactory.getLogger(
      OfferServiceImpl.class);

  @Autowired public OfferServiceImpl(OfferRepository offerRepository,
      BusinessRepository businessRepository, MongoTemplate mongoTemplate)
  {
    this.offerRepository = offerRepository;
    this.businessRepository = businessRepository;
    this.mongoTemplate = mongoTemplate;
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

    Business business = businessRepository.findById(request.getBusinessId())
        .get();

    // Save the updated document (will replace the existing one)
    Offer updatedOffer = offerRepository.save(
        generateOfferFromOfferResponse(request, business));
    OfferResponse offerResponse = buildOfferResponse(
        updatedOffer); // method to build the response

    responseObserver.onNext(offerResponse);
    responseObserver.onCompleted();

  }

  @Override public void getOffersByBusinessId(OfferIdRequest request,
      StreamObserver<OfferListResponse> responseObserver)
  {
    logger.info("Request for offers by id: {}", request);

    List<Offer> offersByBusinessId = offerRepository.findByBusinessId(
        request.getId());

    System.out.println(offersByBusinessId.getFirst());
    responseObserver.onNext(
        buildOfferListResponseFromOffersList(offersByBusinessId));
    responseObserver.onCompleted();

  }


  @Override public void getOffers(FilterRequest request,
      StreamObserver<OfferListResponse> responseObserver)
  {
    System.out.println("Requests: " + request.toString());

    Query offerQuery = new Query();

    // Filter by status
    offerQuery.addCriteria(
        Criteria.where("status").is(OfferStatus.AVAILABLE.getStatus()));

    // Filter by price range
    if (request.hasMinOfferPrice() || request.hasMaxOfferPrice())
    {
      Criteria priceCriteria = Criteria.where("offer_price");

      if (request.hasMinOfferPrice())
      {
        priceCriteria = priceCriteria.gte(request.getMinOfferPrice());
      }
      if (request.hasMaxOfferPrice())
      {
        priceCriteria = priceCriteria.lte(request.getMaxOfferPrice());
      }

      offerQuery.addCriteria(priceCriteria);
    }

    // Filter by pickup time range
    if (request.hasPickupTimeStart())
    {
      offerQuery.addCriteria(Criteria.where("pickup_time_start").gte(
          DateTimeConverter.convertProtoTimestamp_To_LocalDateTime(
              request.getPickupTimeStart())));
    }
    if (request.hasPickupTimeEnd())
    {
      offerQuery.addCriteria(Criteria.where("pickup_time_end").lte(
          DateTimeConverter.convertProtoTimestamp_To_LocalDateTime(
              request.getPickupTimeEnd())));
    }

    // Filter by categories
    if (!request.getCategoriesList().isEmpty())
    {
      offerQuery.addCriteria(
          Criteria.where("categories").in(request.getCategoriesList()));
    }

    // Filter by location
    if (request.hasLocation())
    {
      Location location = request.getLocation();
      GeoJsonPoint locationPoint = new GeoJsonPoint(location.getLongitude(),
          location.getLatitude());
      double maxDistanceMeters =
          location.getRadius() * 1000; // Convert radius to meters

      // Query the businesses within the location radius
      Query businessQuery = new Query();
      businessQuery.addCriteria(
          Criteria.where("location").nearSphere(locationPoint)
              .maxDistance(maxDistanceMeters));

      // Fetch matching business IDs
      List<ObjectId> businessIds = mongoTemplate.find(businessQuery,
              Business.class).stream()
          .map(business -> new ObjectId(business.getId()))
          .collect(Collectors.toList());

      // Filter offers by the matching business IDs
      offerQuery.addCriteria(Criteria.where("business_id.$id").in(businessIds));

    }

    if (request.hasTextSearch())
    {
      String textSearch = request.getTextSearch();
      offerQuery.addCriteria(new Criteria().orOperator(
          Criteria.where("title").regex(textSearch, "i"), // Match in title
          Criteria.where("business.business_name").regex(textSearch, "i")
          // Match in business name
      ));
    }

    System.out.println("Final query: " + offerQuery);
    // Fetch filtered offers
    List<Offer> filteredOffers = mongoTemplate.find(offerQuery, Offer.class);

    System.out.println("FilteredOffers");

    for (Offer o : filteredOffers)
    {
      System.out.println("FilteredOffer: " + o.toString());
    }

    // Sort by creation time (desc)
    filteredOffers = filteredOffers.stream().sorted(
            (o1, o2) -> o2.getCreationTime().compareTo(o1.getCreationTime()))
        .toList();

    // Build and send response
    OfferListResponse response = buildOfferListResponseFromOffersList(
        filteredOffers);
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

}

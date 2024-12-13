package com.example.data_server.service;

import com.example.data_server.repository.BusinessRepository;
import com.example.data_server.repository.CustomerRepository;
import com.example.data_server.repository.OfferRepository;
import com.example.data_server.utility.GeoUtils;
import com.example.sep3.grpc.*;
import com.example.shared.converters.DateTimeConverter;
import com.example.shared.entities.domainEntities.Offer;
import com.example.shared.entities.usersEntities.Business;
import com.example.shared.entities.usersEntities.Customer;
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

import java.awt.*;
import java.time.LocalDateTime;
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
  private CustomerRepository customerRepository;
  private final MongoTemplate mongoTemplate;

  private static final Logger logger = LoggerFactory.getLogger(
      OfferServiceImpl.class);

  @Autowired public OfferServiceImpl(OfferRepository offerRepository,
                                     CustomerRepository customerRepository,
                                     BusinessRepository businessRepository, MongoTemplate mongoTemplate)
  {
    this.offerRepository = offerRepository;
    this.customerRepository = customerRepository;
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
  @Override public void getOffersByBusinessId(OfferIdRequest request,
        StreamObserver<OfferListResponse> responseObserver){
    logger.info("Request for offers by id: {}", request);

    List<Offer> offersByBusinessId = offerRepository.findByBusinessId(
        request.getId());

    System.out.println(offersByBusinessId.getFirst());
    responseObserver.onNext(
        buildOfferListResponseFromOffersList(offersByBusinessId));
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

  public void getOffersNotProper(FilterRequest request, StreamObserver<OfferListResponse> responseObserver) {
    System.out.println("Requests: " + request.toString());

    Query query = new Query();

    // Filter by status
    query.addCriteria(Criteria.where("status").is(OfferStatus.AVAILABLE.getStatus()));

    // Filter by price range
    if (request.hasMinOfferPrice()) {
      query.addCriteria(Criteria.where("offer_price").gte(request.getMinOfferPrice()));
    }
    if (request.hasMaxOfferPrice()) {
      query.addCriteria(Criteria.where("offer_price").lte(request.getMaxOfferPrice()));
    }

    // Filter by pickup time range
    if (request.hasPickupTimeStart()) {
      query.addCriteria(Criteria.where("pickup_time_start")
              .gte(DateTimeConverter.convertProtoTimestamp_To_LocalDateTime(request.getPickupTimeStart())));
    }
    if (request.hasPickupTimeEnd()) {
      query.addCriteria(Criteria.where("pickup_time_end")
              .lte(DateTimeConverter.convertProtoTimestamp_To_LocalDateTime(request.getPickupTimeEnd())));
    }

    // Filter by categories
    if (!request.getCategoriesList().isEmpty()) {
      query.addCriteria(Criteria.where("categories").in(request.getCategoriesList()));
    }

    // Filter by location
    if (request.hasLocation()) {
      Location location = request.getLocation();
      GeoJsonPoint locationPoint = new GeoJsonPoint(location.getLongitude(), location.getLatitude());
      double maxDistanceMeters = location.getRadius() * 1000; // Convert radius to meters
      query.addCriteria(Criteria.where("business.location").nearSphere(locationPoint).maxDistance(maxDistanceMeters));
    }

    // Fetch results
    List<Offer> filteredOffers = mongoTemplate.find(query, Offer.class);

    // Sort by creation time (desc)
    filteredOffers = filteredOffers.stream()
            .sorted((o1, o2) -> o2.getCreationTime().compareTo(o1.getCreationTime()))
            .toList();

    // Build and send response
    OfferListResponse response = buildOfferListResponseFromOffersList(filteredOffers);
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void getOffers(FilterRequest request, StreamObserver<OfferListResponse> responseObserver) {
    System.out.println("Requests: " + request.toString());

    Query offerQuery = new Query();

    // Filter by status
    offerQuery.addCriteria(Criteria.where("status").is(OfferStatus.AVAILABLE.getStatus()));

    // Filter by price range
    // Filter by price range
    if (request.hasMinOfferPrice() || request.hasMaxOfferPrice()) {
      Criteria priceCriteria = Criteria.where("offer_price");

      if (request.hasMinOfferPrice()) {
        priceCriteria = priceCriteria.gte(request.getMinOfferPrice());
      }
      if (request.hasMaxOfferPrice()) {
        priceCriteria = priceCriteria.lte(request.getMaxOfferPrice());
      }

      offerQuery.addCriteria(priceCriteria);
    }


    // Filter by pickup time range
    if (request.hasPickupTimeStart()) {
      offerQuery.addCriteria(Criteria.where("pickup_time_start")
              .gte(DateTimeConverter.convertProtoTimestamp_To_LocalDateTime(request.getPickupTimeStart())));
    }
    if (request.hasPickupTimeEnd()) {
      offerQuery.addCriteria(Criteria.where("pickup_time_end")
              .lte(DateTimeConverter.convertProtoTimestamp_To_LocalDateTime(request.getPickupTimeEnd())));
    }

    // Filter by categories
    if (!request.getCategoriesList().isEmpty()) {
      offerQuery.addCriteria(Criteria.where("categories").in(request.getCategoriesList()));
    }

    // Filter by location
    if (request.hasLocation()) {
      Location location = request.getLocation();
      GeoJsonPoint locationPoint = new GeoJsonPoint(location.getLongitude(), location.getLatitude());
      double maxDistanceMeters = location.getRadius() * 1000; // Convert radius to meters

      System.out.println("Request longitude: " +location.getLongitude());
      System.out.println("Request latitude: " +location.getLatitude());
      System.out.println("Max distance" + maxDistanceMeters);

      // Query the businesses within the location radius
      Query businessQuery = new Query();
      businessQuery.addCriteria(Criteria.where("location").nearSphere(locationPoint).maxDistance(maxDistanceMeters));

      System.out.println("Business query: "+ businessQuery);
      // Fetch matching business IDs
      List<ObjectId> businessIds = mongoTemplate.find(businessQuery, Business.class)
              .stream()
              .map(business -> new ObjectId(business.getId()))
              .collect(Collectors.toList());


      System.out.println("Businesses ids");
      for (ObjectId id : businessIds){
        System.out.println("Business: " + id.toString());
      }

      // Filter offers by the matching business IDs
      offerQuery.addCriteria(Criteria.where("business_id.$id").in(businessIds));


    }

    if (request.hasTextSearch()) {
      String textSearch = request.getTextSearch();
      offerQuery.addCriteria(new Criteria().orOperator(
              Criteria.where("title").regex(textSearch, "i") // Match in title
//              Criteria.where("description").regex(textSearch, "i"), // Match in description
//              Criteria.where("categories").regex(textSearch, "i"), // Match in categories
//              Criteria.where("business.business_name").regex(textSearch, "i") // Match in business name
      ));
    }

    System.out.println("Final query: "+ offerQuery);
    // Fetch filtered offers
    List<Offer> filteredOffers = mongoTemplate.find(offerQuery, Offer.class);


    System.out.println("FilteredOffers");

    for(Offer o : filteredOffers){
      System.out.println("FilteredOffer: " + o.toString());
    }

    // Sort by creation time (desc)
    filteredOffers = filteredOffers.stream()
            .sorted((o1, o2) -> o2.getCreationTime().compareTo(o1.getCreationTime()))
            .toList();

    // Build and send response
    OfferListResponse response = buildOfferListResponseFromOffersList(filteredOffers);
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }


  public void getOffersOlds(FilterRequest request,
      StreamObserver<OfferListResponse> responseObserver)
  {
    System.out.println("Requests: " + request.toString());
    List<Offer> filteredOffers;



    var allAvailableOffers = offerRepository.findByStatus(
        OfferStatus.AVAILABLE.getStatus());

    filteredOffers = allAvailableOffers.stream()
            .filter(item -> !request.hasMaxOfferPrice() || item.getOfferPrice() <= request.getMaxOfferPrice())
            .filter(item -> !request.hasMinOfferPrice() || item.getOfferPrice() >= request.getMinOfferPrice())
            .filter(item -> !request.hasPickupTimeStart() || !item.getPickupTimeStart()
                    .isBefore(DateTimeConverter.convertProtoTimestamp_To_LocalDateTime(request.getPickupTimeStart())))
            .filter(item -> !request.hasPickupTimeEnd() || item.getPickupTimeEnd()
                    .isAfter(DateTimeConverter.convertProtoTimestamp_To_LocalDateTime(request.getPickupTimeEnd())))
            .filter(item -> request.getCategoriesList().isEmpty() || item.getCategories()
                    .stream().anyMatch(request.getCategoriesList()::contains))
            .filter(item -> {

              if(request.hasLocation()){
                Location location = request.getLocation();

                // Extract location details from the request
                double requestLatitude = location.getLatitude();
                double requestLongitude = location.getLongitude();
                double requestRadius = location.getRadius();

                System.out.println("RequestLatitude: "+ requestLatitude);
                System.out.println("RequestLongitude: "+ requestLongitude);


                // Get the business location from the item
                Business business = item.getBusiness();
                double businessLatitude = business.getLocation().getCoordinates().getLast();
                double businessLongitude = business.getLocation().getCoordinates().getFirst();
                System.out.println("BusinessLatitude: "+ businessLatitude);
                System.out.println("BusinessLongitude: "+ businessLongitude);

                // Calculate the distance between the request location and business location
                double distance = GeoUtils.calculateDistance(requestLatitude, requestLongitude, businessLatitude, businessLongitude);

                System.out.println("Business: " + business.getId());
                System.out.println("Request radius: " + requestRadius);
                System.out.println("Distance: " + distance + " for ID " + item.getId());

                // Include only businesses within the radius
                return distance <= requestRadius;
              }
              return true; // If no location is provided, include all offers
            })
            //.sorted((o1, o2) -> o2.getCreationTime().compareTo(o1.getCreationTime())) // Uncomment to sort by creation time
            .toList();


//    System.out.println("---------------------------");
//    System.out.println("Request pickup time start: "+ request.getPickupTimeStart());
//    System.out.println("Request pickup time end: "+request.getPickupTimeEnd());
//
//    for (Offer offer : filteredOffers)
//    {
//      System.out.println("Offer pickup time start: "+offer.getPickupTimeStart());
//      System.out.println("Offer pickup time end: "+offer.getPickupTimeEnd());
//    }

    filteredOffers = filteredOffers.stream().sorted(
                    (o1, o2) -> o2.getCreationTime().compareTo(o1.getCreationTime()))
            .toList();
    //    var test = offerRepository.findAll();
    //    filteredOffers = test.stream().filter(
    //        offer -> offer.getCategories().stream()
    //            .anyMatch(request.getCategoriesList()::contains)).toList();

    // Build the response
    OfferListResponse offers = buildOfferListResponseFromOffersList(filteredOffers);
//    logger.info("Sending SaveOfferResponse: {}", offers);

    responseObserver.onNext(offers);
    responseObserver.onCompleted();
  }

}

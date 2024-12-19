package com.example.data_server.service;
import com.example.data_server.repository.BusinessRepository;

import com.example.shared.entities.userEntities.Business;
import com.example.shared.model.Address;
import com.google.protobuf.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import com.example.data_server.repository.OfferRepository;
import com.example.sep3.grpc.CreateOfferRequest;
import com.example.sep3.grpc.OfferResponse;
import com.example.shared.entities.domainEntities.Offer;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class OfferServiceImplTest {

  @Mock private BusinessRepository businessRepository;
  @Mock private OfferRepository offerRepository;

  @InjectMocks
  private OfferServiceImpl OfferServiceImpl;  // The service being tested

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void Test_AddOffer_CorrectCase() throws IOException {
    //  Mock Categories
    ArrayList<String> categories = new ArrayList<>();
    categories.add("Vegetarian");

    //  Mock Address
    Address address = new Address();
    address.setCity("San Francisco");
    address.setCountry("USA");
    address.setPostalCode("9410");
    address.setState("CA");
    address.setCounty("Somewhere");
    address.setNumber("5");
    address.setStreet("Grove Street");

    // Mock Business
    Business business = new Business();
    business.setId("6761d2a58bdddb3d7617b01b");
    business.setBusinessName("testBusinessName");
    business.setLogoPath("6764a60b-cb96-453f-adfb-cb1b8bf5b602");
    business.setAddress(address);
    business.setEmail("dummyemail193553330@example.com");

    // Mock Offer
    Offer offer = new Offer();
    offer.setId("offer123"); // Mock ID generation
    offer.setTitle("TestTitle");
    offer.setStatus("available");
    offer.setDescription("This is a dummy description");
    offer.setOriginalPrice(50);
    offer.setOfferPrice(20);
    offer.setNumberOfItems(5);
    offer.setNumberOfAvailableItems(4);
    offer.setImagePath("8762f962-3a91-44fd-98d8-800fcbaa497f");
    offer.setBusiness(business);
    offer.setCategories(categories);
    offer.setPickupTimeStart(LocalDateTime.of(2024, 12, 18, 12, 0));
    offer.setPickupTimeEnd(LocalDateTime.of(2024, 12, 19, 12, 0));
    offer.setCreationTime(LocalDateTime.now());

    // Create the request
    CreateOfferRequest request = CreateOfferRequest.newBuilder()
            .setTitle("TestTitle")
            .setDescription("This is a dummy description")
            .setOriginalPrice(50)
            .setOfferPrice(20)
            .setNumberOfItems(5)
            .setPickupTimeStart(convertDateTime_To_TimeStamp(2024, 12, 18, 12, 0))
            .setPickupTimeEnd(convertDateTime_To_TimeStamp(2024, 12, 19, 12, 0))
            .setNumberOfAvailableItems(4)
            .setBusinessId("6761d2a58bdddb3d7617b01b")
            .build();
    // Mock the response observer
    StreamObserver<OfferResponse> responseObserver = mock(StreamObserver.class);

    // Call the createOffer method
    OfferServiceImpl.createOffer(request, responseObserver);

    // Verify the interactions
    verify(responseObserver).onNext(argThat(response ->
            response.getTitle().equals("TestTitle") &&
                    response.getDescription().equals("This is a dummy description") &&
                    response.getOriginalPrice() == 50 &&
                    response.getOfferPrice() == 20 &&
                    response.getBusinessId().equals("6761d2a58bdddb3d7617b01b") &&
                    response.getId().equals("offer123") // Verify the mocked ID
    ));
    // Mock BusinessRepository behavior
    when(businessRepository.findById("6761d2a58bdddb3d7617b01b"))
            .thenReturn(Optional.of(business));

    // Mock OfferRepository behavior
    when(offerRepository.save(any(Offer.class)))
            .thenReturn(offer);
    verify(responseObserver).onCompleted();
  }

  @Test
  void Test_AddOffer_NoBusiness() {
    CreateOfferRequest request = CreateOfferRequest.newBuilder()
            .setTitle("TestTitle")
            .setDescription("This is a dummy description")
            .setOriginalPrice(50)
            .setOfferPrice(20)
            .setNumberOfItems(5)
            .setPickupTimeStart(convertDateTime_To_TimeStamp(2024, 12, 18, 12, 0))
            .setPickupTimeEnd(convertDateTime_To_TimeStamp(2024, 12, 19, 12, 0))
            .setNumberOfAvailableItems(4)
            .setBusinessId("nonExistentBusinessId")
            .build();

    StreamObserver<OfferResponse> responseObserver = mock(StreamObserver.class);

    when(businessRepository.findById("nonExistentBusinessId"))
            .thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class, () -> {
      OfferServiceImpl.createOffer(request, responseObserver);
    });

    verify(responseObserver, never()).onNext(any());
    verify(responseObserver, never()).onCompleted();
  }
  @Test
  void Test_AddOffer_WithNullTitle() {
    //  Mock Categories
    ArrayList<String> categories = new ArrayList<>();
    categories.add("Vegetarian");

    // Mock Address
    Address address = new Address();
    address.setCity("San Francisco");
    address.setCountry("USA");
    address.setPostalCode("9410");
    address.setState("CA");
    address.setCounty("Somewhere");
    address.setNumber("5");
    address.setStreet("Grove Street");

    // Mock Business
    Business business = new Business();
    business.setId("6761d2a58bdddb3d7617b01b");
    business.setBusinessName("testBusinessName");
    business.setLogoPath("6764a60b-cb96-453f-adfb-cb1b8bf5b602");
    business.setAddress(address);
    business.setEmail("dummyemail193553330@example.com");

    // Mock Offer
    Offer offer = new Offer();
    offer.setId("offer123"); // Mock ID generation
    offer.setTitle("TestTitle");
    offer.setStatus("available");
    offer.setDescription("This is a dummy description");
    offer.setOriginalPrice(0);
    offer.setOfferPrice(0);
    offer.setNumberOfItems(0);
    offer.setNumberOfAvailableItems(4);
    offer.setImagePath("8762f962-3a91-44fd-98d8-800fcbaa497f");
    offer.setBusiness(business);
    offer.setCategories(categories);
    offer.setPickupTimeStart(LocalDateTime.of(2024, 12, 18, 12, 0));
    offer.setPickupTimeEnd(LocalDateTime.of(2024, 12, 19, 12, 0));
    offer.setCreationTime(LocalDateTime.now());

    //  Mock BusinessRepository behaviour
    when(businessRepository.findById("6761d2a58bdddb3d7617b01b"))
            .thenReturn(Optional.of(business));
    // Mock OfferRepository behavior
    when(offerRepository.save(any(Offer.class)))
            .thenReturn(offer);

    CreateOfferRequest request = CreateOfferRequest.newBuilder()
            .setTitle(null)
            .setDescription("Boundary values")
            .setOriginalPrice(20)  // Boundary condition
            .setOfferPrice(5)     // Boundary condition
            .setNumberOfItems(4)  // Boundary condition
            .setPickupTimeStart(convertDateTime_To_TimeStamp(2024, 12, 19, 12, 0)) // Start
            .setPickupTimeEnd(convertDateTime_To_TimeStamp(2024, 12, 18, 12, 0))   // End before start
            .setNumberOfAvailableItems(0) // Boundary condition
            .setBusinessId("6761d2a58bdddb3d7617b01b")
            .build();



    StreamObserver<OfferResponse> responseObserver = mock(StreamObserver.class);

    assertThrows(IllegalArgumentException.class, () -> {
      OfferServiceImpl.createOffer(request, responseObserver);
    });

    verify(responseObserver, never()).onNext(any());
    verify(responseObserver, never()).onCompleted();
  }
  @Test
  void Test_AddOffer_Boundary() {
    //  Mock Categories
    ArrayList<String> categories = new ArrayList<>();
    categories.add("Vegetarian");

    // Mock Address
    Address address = new Address();
    address.setCity("San Francisco");
    address.setCountry("USA");
    address.setPostalCode("9410");
    address.setState("CA");
    address.setCounty("Somewhere");
    address.setNumber("5");
    address.setStreet("Grove Street");

    // Mock Business
    Business business = new Business();
    business.setId("6761d2a58bdddb3d7617b01b");
    business.setBusinessName("testBusinessName");
    business.setLogoPath("6764a60b-cb96-453f-adfb-cb1b8bf5b602");
    business.setAddress(address);
    business.setEmail("dummyemail193553330@example.com");

    // Mock Offer
    Offer offer = new Offer();
    offer.setId("offer123"); // Mock ID generation
    offer.setTitle("TestTitle");
    offer.setStatus("available");
    offer.setDescription("This is a dummy description");
    offer.setOriginalPrice(0);
    offer.setOfferPrice(0);
    offer.setNumberOfItems(0);
    offer.setNumberOfAvailableItems(4);
    offer.setImagePath("8762f962-3a91-44fd-98d8-800fcbaa497f");
    offer.setBusiness(business);
    offer.setCategories(categories);
    offer.setPickupTimeStart(LocalDateTime.of(2024, 12, 18, 12, 0));
    offer.setPickupTimeEnd(LocalDateTime.of(2024, 12, 19, 12, 0));
    offer.setCreationTime(LocalDateTime.now());

    when(businessRepository.findById("6761d2a58bdddb3d7617b01b"))
            .thenReturn(Optional.of(business));

    CreateOfferRequest request = CreateOfferRequest.newBuilder()
            .setTitle("Boundary Test")
            .setDescription("Boundary values")
            .setOriginalPrice(0)  // Boundary condition
            .setOfferPrice(0)     // Boundary condition
            .setNumberOfItems(0)  // Boundary condition
            .setPickupTimeStart(convertDateTime_To_TimeStamp(2024, 12, 19, 12, 0)) // Start
            .setPickupTimeEnd(convertDateTime_To_TimeStamp(2024, 12, 18, 12, 0))   // End before start
            .setNumberOfAvailableItems(0) // Boundary condition
            .setBusinessId("6761d2a58bdddb3d7617b01b")
            .build();

    // Mock OfferRepository behavior
    when(offerRepository.save(any(Offer.class)))
            .thenReturn(offer);

    StreamObserver<OfferResponse> responseObserver = mock(StreamObserver.class);

    assertThrows(IllegalArgumentException.class, () -> {
      OfferServiceImpl.createOffer(request, responseObserver);
    });

    verify(responseObserver, never()).onNext(any());
    verify(responseObserver, never()).onCompleted();
  }
  @Test
  void Test_AddOffer_WithInvalidPriceRange() {
    // Mock Categories
    ArrayList<String> categories = new ArrayList<>();
    categories.add("Vegetarian");

    // Mock Address
    Address address = new Address();
    address.setCity("San Francisco");
    address.setCountry("USA");
    address.setPostalCode("9410");
    address.setState("CA");
    address.setCounty("Somewhere");
    address.setNumber("5");
    address.setStreet("Grove Street");

    // Mock Business
    Business business = new Business();
    business.setId("6761d2a58bdddb3d7617b01b");
    business.setBusinessName("testBusinessName");
    business.setLogoPath("6764a60b-cb96-453f-adfb-cb1b8bf5b602");
    business.setAddress(address);
    business.setEmail("dummyemail193553330@example.com");

    // Mock Offer with invalid pricing
    Offer offer = new Offer();
    offer.setId("offer123");
    offer.setTitle("TestTitle");
    offer.setStatus("available");
    offer.setDescription("This is a dummy description");
    offer.setOriginalPrice(30);  // Original price is 30
    offer.setOfferPrice(40);     // Offer price is 40 (higher than original)
    offer.setNumberOfItems(5);
    offer.setNumberOfAvailableItems(4);
    offer.setImagePath("8762f962-3a91-44fd-98d8-800fcbaa497f");
    offer.setBusiness(business);
    offer.setCategories(categories);
    offer.setPickupTimeStart(LocalDateTime.of(2024, 12, 18, 12, 0));
    offer.setPickupTimeEnd(LocalDateTime.of(2024, 12, 19, 12, 0));
    offer.setCreationTime(LocalDateTime.now());

    when(businessRepository.findById("6761d2a58bdddb3d7617b01b"))
            .thenReturn(Optional.of(business));

    CreateOfferRequest request = CreateOfferRequest.newBuilder()
            .setTitle("TestTitle")
            .setDescription("This is a dummy description")
            .setOriginalPrice(30)
            .setOfferPrice(40) // Invalid price range
            .setNumberOfItems(5)
            .setPickupTimeStart(convertDateTime_To_TimeStamp(2024, 12, 18, 12, 0))
            .setPickupTimeEnd(convertDateTime_To_TimeStamp(2024, 12, 19, 12, 0))
            .setNumberOfAvailableItems(4)
            .setBusinessId("6761d2a58bdddb3d7617b01b")
            .build();

    StreamObserver<OfferResponse> responseObserver = mock(StreamObserver.class);

    assertThrows(IllegalArgumentException.class, () -> {
      OfferServiceImpl.createOffer(request, responseObserver);
    });

    verify(responseObserver, never()).onNext(any());
    verify(responseObserver, never()).onCompleted();
  }

  @Test
  void Test_AddOffer_BoundaryMaxValues() {
    // Mock Categories
    ArrayList<String> categories = new ArrayList<>();
    categories.add("Vegetarian");

    // Mock Address
    Address address = new Address();
    address.setCity("San Francisco");
    address.setCountry("USA");
    address.setPostalCode("9410");
    address.setState("CA");
    address.setCounty("Somewhere");
    address.setNumber("5");
    address.setStreet("Grove Street");

    // Mock Business
    Business business = new Business();
    business.setId("6761d2a58bdddb3d7617b01b");
    business.setBusinessName("testBusinessName");
    business.setLogoPath("6764a60b-cb96-453f-adfb-cb1b8bf5b602");
    business.setAddress(address);
    business.setEmail("dummyemail193553330@example.com");

    // Boundary: Max values for price, number of items, and available items
    Offer offer = new Offer();
    offer.setId("offer123");
    offer.setTitle("Boundary Test - Max Values");
    offer.setStatus("available");
    offer.setDescription("This is a boundary test with max values.");
    offer.setOriginalPrice(Integer.MAX_VALUE);  // Max possible integer value
    offer.setOfferPrice(Integer.MAX_VALUE);     // Max possible integer value
    offer.setNumberOfItems(Integer.MAX_VALUE);  // Max possible integer value
    offer.setNumberOfAvailableItems(Integer.MAX_VALUE); // Max available items
    offer.setImagePath("8762f962-3a91-44fd-98d8-800fcbaa497f");
    offer.setBusiness(business);
    offer.setCategories(categories);
    offer.setPickupTimeStart(LocalDateTime.of(2024, 12, 18, 12, 0));
    offer.setPickupTimeEnd(LocalDateTime.of(2024, 12, 19, 12, 0));
    offer.setCreationTime(LocalDateTime.now());

    when(businessRepository.findById("6761d2a58bdddb3d7617b01b"))
            .thenReturn(Optional.of(business));

    CreateOfferRequest request = CreateOfferRequest.newBuilder()
            .setTitle("Boundary Test - Max Values")
            .setDescription("This is a boundary test with max values.")
            .setOriginalPrice(Integer.MAX_VALUE)  // Max values
            .setOfferPrice(Integer.MAX_VALUE)
            .setNumberOfItems(Integer.MAX_VALUE)
            .setPickupTimeStart(convertDateTime_To_TimeStamp(2024, 12, 18, 12, 0))
            .setPickupTimeEnd(convertDateTime_To_TimeStamp(2024, 12, 19, 12, 0))
            .setNumberOfAvailableItems(Integer.MAX_VALUE)
            .setBusinessId("6761d2a58bdddb3d7617b01b")
            .build();

    StreamObserver<OfferResponse> responseObserver = mock(StreamObserver.class);

    when(offerRepository.save(any(Offer.class)))
            .thenReturn(offer);

    OfferServiceImpl.createOffer(request, responseObserver);

    verify(responseObserver).onNext(argThat(response ->
            response.getOriginalPrice() == Integer.MAX_VALUE &&
                    response.getOfferPrice() == Integer.MAX_VALUE &&
                    response.getNumberOfItems() == Integer.MAX_VALUE &&
                    response.getNumberOfAvailableItems() == Integer.MAX_VALUE
    ));
    verify(responseObserver).onCompleted();
  }

  private Timestamp convertDateTime_To_TimeStamp(int year, int month, int day, int hour, int minute) {
      LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute, 0);
      return Timestamp.newBuilder()
              .setSeconds(dateTime.toEpochSecond(ZoneOffset.UTC))
              .build();
  }
}
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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.data_server.converters.OfferEntityGrpcConverter.generateOfferFromCreateOfferRequest;
import static org.mockito.Mockito.*;

//TODO: Tests are not working, need to fix them; methods should be
// tested individually in BloomRPC
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
  void testAddOffer() throws IOException {
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

    // Mock BusinessRepository behavior
    Business business = new Business();
    business.setId("6761d2a58bdddb3d7617b01b");
    business.setBusinessName("testBusinessName");
    business.setLogoPath("6764a60b-cb96-453f-adfb-cb1b8bf5b602");
    business.setAddress(address);
    business.setEmail("dummyemail193553330@example.com");
    when(businessRepository.findById("6761d2a58bdddb3d7617b01b"))
            .thenReturn(Optional.of(business));

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

// Prepare the Offer object that will be returned by the mock repository
    Offer offer = new Offer();
    offer.setId("offer123"); // Mock ID generation
    offer.setTitle("TestTitle");
    offer.setStatus("available");
    offer.setDescription("This is a dummy description");
    offer.setOriginalPrice(50);
    offer.setOfferPrice(20);
    offer.setNumberOfItems(5);
    offer.setNumberOfAvailableItems(4);
    offer.setImagePath(request.getImagePath());
    offer.setBusiness(business);
    offer.setCategories(categories);
    offer.setPickupTimeStart(LocalDateTime.of(2024, 12, 18, 12, 0)); // Use the start time from the request
    offer.setPickupTimeEnd(LocalDateTime.of(2024, 12, 19, 12, 0));   // Use the end time from the request
    offer.setCreationTime(LocalDateTime.now()); // Mock current timestamp as creation time



    // Mock OfferRepository behavior
    when(offerRepository.save(any(Offer.class))).thenReturn(offer);

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
    verify(responseObserver).onCompleted();
  }


  private Timestamp convertDateTime_To_TimeStamp(int year, int month, int day, int hour, int minute) {
      LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute, 0);
      return Timestamp.newBuilder()
              .setSeconds(dateTime.toEpochSecond(ZoneOffset.UTC))
              .build();
  }
}
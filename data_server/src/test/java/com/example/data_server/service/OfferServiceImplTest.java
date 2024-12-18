package com.example.data_server.service;
import com.example.data_server.repository.BusinessRepository;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//TODO: Tests are not working, need to fix them; methods should be
// tested individually in BloomRPC
class OfferServiceImplTest {

  @InjectMocks
  private OfferServiceImpl offerService;  // The service being tested

  @MockBean
  private BusinessRepository businessRepository;
  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testAddOffer() throws IOException {
    // Create a mock AddOfferRequest
    List<String> categories = new ArrayList<>();
    categories.add("Vegetarian");


      CreateOfferRequest request = CreateOfferRequest.newBuilder()
              .setTitle("TestTitle")
              .setDescription("This is a dummy description")
              .setOriginalPrice(50)
              .setOfferPrice(20)
              .setNumberOfItems(5)
              .setPickupTimeStart(convertDateTime_To_TimeStamp(2024,12,18,12,0))
              .setPickupTimeEnd(convertDateTime_To_TimeStamp(2024,12,19,12,0))
              .setNumberOfAvailableItems(4)
              .setBusinessId("67606413c6fdf1741ded72ae").build();

    // Mock the response observer
  StreamObserver<OfferResponse> responseObserver = Mockito.mock(StreamObserver.class);

    // Call the addOffer method
    offerService.createOffer(request, responseObserver);

    // Verify the response
    verify(responseObserver).onNext(any(OfferResponse.class));
    verify(responseObserver).onCompleted();
  }

  private Timestamp convertDateTime_To_TimeStamp(int year, int month, int day, int hour, int minute) {
      LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute, 0);
      return Timestamp.newBuilder()
              .setSeconds(dateTime.toEpochSecond(ZoneOffset.UTC))
              .build();
  }
}
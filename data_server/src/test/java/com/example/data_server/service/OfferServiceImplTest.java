package com.example.data_server.service;

//TODO: Tests are not working, need to fix them; methods should be
// tested individually in BloomRPC
/*
class OfferServiceImplTest {

  @Mock private OfferRepository offerRepository;

  @InjectMocks private OfferServiceImpl offerService;

  @BeforeEach void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test void testAddOffer() throws IOException {
    // Create a mock AddOfferRequest
    List<String> categories = new ArrayList<>(List.of("Category1"));
    SaveOfferRequest request = SaveOfferRequest.newBuilder()
        .setTitle("Test Offer").setDescription("Test Description")
        .setOfferPrice(80).setOriginalPrice(100).setPickupDate(
            DateTimeConverter.convertDateDaoToGrpcDate(
                DateTimeConverter.getCurrentDateDao())).setPickupTimeStart(
            DateTimeConverter.convertTimeDaoToGrpcTime(
                DateTimeConverter.getCurrentTimeDao())).setPickupTimeEnd(
            DateTimeConverter.convertTimeDaoToGrpcTime(
                DateTimeConverter.getCurrentTimeDao()))
        //.addAllCategories(categories)
        .setNumberOfItems(10).build();

    // Mock the response observer
    StreamObserver<SaveOfferResponse> responseObserver = mock(
        StreamObserver.class);

    // Call the addOffer method
    offerService.saveOffer(request, responseObserver);

    // Verify the response
    verify(responseObserver).onNext(any(SaveOfferResponse.class));
    verify(responseObserver).onCompleted();
  }
}*/
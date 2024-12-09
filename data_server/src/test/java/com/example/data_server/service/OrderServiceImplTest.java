package com.example.data_server.service;

import static org.mockito.ArgumentMatchers.any;

//TODO: Tests are not working, need to fix them; methods should be
// tested individually in BloomRPC
/*
class OrderServiceImplTest {

  @Mock private PurchaseRepository orderRepository;
  @Mock private OfferRepository offerRepository;

  @InjectMocks private PurchaseServiceImpl orderService;
  @InjectMocks private OfferServiceImpl offerService;

  @BeforeEach void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
void testAddOrder() {
    // Create an offer
    Offer offer = new Offer();
    offer.setOriginalPrice(100);
    offer.setOfferPrice(80);

    // Mock the OfferRepository to save and return the created offer
    when(offerRepository.save(any(Offer.class))).thenAnswer(invocation -> invocation.getArgument(0));

    // Save the offer
    Offer savedOffer = offerRepository.save(offer);

    // Mock the PurchaseRepository to save and return the created order
    when(orderRepository.save(any(Purchase.class))).thenAnswer(invocation -> invocation.getArgument(0));

    // Create the AddOrderRequest
    AddOrderRequest request = AddOrderRequest.newBuilder()
        .setUserId("user123")
        .setNumberOfItems(2)
        .setOfferId("673704d2df30482589885cf5")
        .build();

    // Mock the response observer
    StreamObserver<OrderResponse> responseObserver = mock(StreamObserver.class);

    // Call the addOrder method
    orderService.addOrder(request, responseObserver);

    // Verify the response
    verify(responseObserver).onNext(any(OrderResponse.class));
    verify(responseObserver).onCompleted();
}

  @Test void testGetOrderById() {
    Purchase Order = new Purchase();
    Order.setId("Order123");
    Order.setUserId("user123");
    Order.setNumberOfItems(2);
    Order.setOrderDate(DateTimeConverter.getCurrentDateDao());

    // Add the Order to the repository
    orderRepository.save(Order);

    when(orderRepository.findById("Order123")).thenReturn(Optional.of(Order));

    OrderIdRequest request = OrderIdRequest.newBuilder().setId("Order123")
        .build();

    StreamObserver<OrderResponse> responseObserver = mock(StreamObserver.class);
    orderService.getOrderById(request, responseObserver);

    verify(responseObserver).onNext(argThat(
        response -> response.getId().equals("Order123") && response.getUserId()
            .equals("user123") && response.getNumberOfItems() == 2
            && response.getOrderDate().getYear()
            == DateTimeConverter.convertDateDaoToGoogleDate(
            Order.getOrderDate()).getYear()
            && response.getOrderDate().getMonth()
            == DateTimeConverter.convertDateDaoToGoogleDate(
            Order.getOrderDate()).getMonth() && response.getOrderDate().getDay()
            == DateTimeConverter.convertDateDaoToGoogleDate(
            Order.getOrderDate()).getDay()));
    verify(responseObserver).onCompleted();
  }

  @Test void testGetAllOrders() {
    Purchase Order1 = new Purchase();
    Order1.setId("Order123");
    Order1.setUserId("user123");
    Order1.setNumberOfItems(2);
    Order1.setOrderDate(DateTimeConverter.getCurrentDateDao());

    Purchase Order2 = new Purchase();
    Order2.setId("Order456");
    Order2.setUserId("user456");
    Order2.setNumberOfItems(3);
    Order2.setOrderDate(DateTimeConverter.getCurrentDateDao());

    when(orderRepository.findAll()).thenReturn(List.of(Order1, Order2));

    StreamObserver<OrderList> responseObserver = mock(StreamObserver.class);
    orderService.getAllOrders(EmptyMessage.newBuilder().build(), responseObserver);

    verify(responseObserver).onNext(any(OrderList.class));
    verify(responseObserver).onCompleted();
  }
}*/
package com.example.data_server.service;

import com.example.data_server.repository.*;
import com.example.data_server.service.PurchaseServiceImpl;
import com.example.shared.entities.domainEntities.*;
import com.example.shared.entities.userEntities.*;
import com.example.sep3.grpc.*;
import com.example.shared.model.Address;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PurchaseServiceImplTest {

    @Mock private PurchaseRepository purchaseRepository;
    @Mock private OfferRepository offerRepository;
    @Mock private BusinessRepository businessRepository;
    @Mock private CustomerRepository customerRepository;

    @InjectMocks private PurchaseServiceImpl purchaseServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePurchase_Success() {
        //  Mock Categories
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Vegetarian");

        //  Mock Address
        com.example.shared.model.Address address = new Address();
        address.setCity("San Francisco");
        address.setCountry("USA");
        address.setPostalCode("9410");
        address.setState("CA");
        address.setCounty("Somewhere");
        address.setNumber("5");
        address.setStreet("Grove Street");

        // Mock Business
        Business business = new Business();
        business.setId("6763e4cb289e2a312b5ce905");
        business.setBusinessName("testBusinessName");
        business.setLogoPath("6764a60b-cb96-453f-adfb-cb1b8bf5b602");
        business.setAddress(address);
        business.setEmail("dummyemail193553330@example.com");

        // Mock Offer
        Offer offer = new Offer();
        offer.setId("675c0e5a8b9c0b678cb3f925"); // Mock ID generation
        offer.setTitle("TestTitle");
        offer.setStatus("available");
        offer.setDescription("This is a dummy description");
        offer.setOriginalPrice(50);
        offer.setOfferPrice(20);
        offer.setNumberOfItems(5);
        offer.setNumberOfAvailableItems(6);
        offer.setImagePath("8762f962-3a91-44fd-98d8-800fcbaa497f");
        offer.setBusiness(business);
        offer.setCategories(categories);
        offer.setPickupTimeStart(LocalDateTime.of(2024, 12, 18, 12, 0));
        offer.setPickupTimeEnd(LocalDateTime.of(2024, 12, 19, 12, 0));
        offer.setCreationTime(LocalDateTime.now());

        // Mock Customer
        Customer customer = new Customer();
        customer.setId("67506015b74df22a32206277");
        customer.setEmail("dummyemail193553330@example.com");
        customer.setFirstName("TestFirstName");
        customer.setLastName("TestLastName");
        customer.setPhoneNumber("123456789");
        customer.setPassword("marty");
        customer.setRole("CUSTOMER");

        // Mock Purchase
        Purchase purchase = new Purchase();
        purchase.setId("purchase123");
        purchase.setCustomer(customer);
        purchase.setBusiness(business);
        purchase.setOffer(offer);
        purchase.setPurchaseTime(LocalDateTime.now());
        purchase.setNumberOfItems(4);
        purchase.setPricePerItem(20);

        // Request
        CreatePurchaseRequest request = CreatePurchaseRequest.newBuilder()
                .setOfferId("675c0e5a8b9c0b678cb3f925")
                .setCustomerId("67506015b74df22a32206277")
                .setNumberOfItems(4)
                .build();

        StreamObserver<PurchaseResponse> responseObserver = mock(StreamObserver.class);

        // Mock Repositories
        when(offerRepository.findById("675c0e5a8b9c0b678cb3f925")).thenReturn(Optional.of(offer));
        when(businessRepository.findById("6763e4cb289e2a312b5ce905")).thenReturn(Optional.of(business));
        when(customerRepository.findById("67506015b74df22a32206277")).thenReturn(Optional.of(customer));
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(purchase);


        // Execute
        purchaseServiceImpl.createPurchase(request, responseObserver);

        // Verify interactions
        verify(offerRepository).updateNumberOfAvailableItems("675c0e5a8b9c0b678cb3f925", 5);
        verify(responseObserver).onNext(argThat(response -> response.getId().equals("purchase123")));
        verify(responseObserver).onCompleted();
    }


    @Test
    void testCreatePurchase_InvalidOffer() {
        // Request
        CreatePurchaseRequest request = CreatePurchaseRequest.newBuilder()
                .setOfferId("invalidOffer")
                .setCustomerId("customer123")
                .setNumberOfItems(5)
                .build();

        StreamObserver<PurchaseResponse> responseObserver = mock(StreamObserver.class);

        // Mock Repositories
        when(offerRepository.findById("invalidOffer")).thenReturn(Optional.empty());

        // Execute and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            purchaseServiceImpl.createPurchase(request, responseObserver);
        });

        // Verify no interactions with responseObserver
        verify(responseObserver, never()).onNext(any());
        verify(responseObserver, never()).onCompleted();
    }

    @Test
    void testCreatePurchase_InvalidCustomer() {
        // Mock Offer
        Offer offer = new Offer();
        offer.setId("offer123");
        offer.setNumberOfAvailableItems(10);

        // Mock Business
        Business business = new Business();
        business.setId("business123");

        offer.setBusiness(business);

        // Request
        CreatePurchaseRequest request = CreatePurchaseRequest.newBuilder()
                .setOfferId("offer123")
                .setCustomerId("invalidCustomer")
                .setNumberOfItems(5)
                .build();

        StreamObserver<PurchaseResponse> responseObserver = mock(StreamObserver.class);

        // Mock Repositories
        when(offerRepository.findById("offer123")).thenReturn(Optional.of(offer));
        when(businessRepository.findById("business123")).thenReturn(Optional.of(business));
        when(customerRepository.findById("invalidCustomer")).thenReturn(Optional.empty());

        // Execute and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            purchaseServiceImpl.createPurchase(request, responseObserver);
        });

        // Verify no interactions with responseObserver
        verify(responseObserver, never()).onNext(any());
        verify(responseObserver, never()).onCompleted();
    }

    @Test
    void testCreatePurchase_InsufficientOfferAvailability() {
        // Mock Offer
        Offer offer = new Offer();
        offer.setId("offer123");
        offer.setNumberOfAvailableItems(3);

        // Mock Business
        Business business = new Business();
        business.setId("business123");

        offer.setBusiness(business);

        // Mock Customer
        Customer customer = new Customer();
        customer.setId("customer123");

        // Request
        CreatePurchaseRequest request = CreatePurchaseRequest.newBuilder()
                .setOfferId("offer123")
                .setCustomerId("customer123")
                .setNumberOfItems(5)
                .build();

        StreamObserver<PurchaseResponse> responseObserver = mock(StreamObserver.class);

        // Mock Repositories
        when(offerRepository.findById("offer123")).thenReturn(Optional.of(offer));
        when(businessRepository.findById("business123")).thenReturn(Optional.of(business));
        when(customerRepository.findById("customer123")).thenReturn(Optional.of(customer));

        // Execute and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            purchaseServiceImpl.createPurchase(request, responseObserver);
        });

        // Verify no interactions with responseObserver
        verify(responseObserver, never()).onNext(any());
        verify(responseObserver, never()).onCompleted();
    }
}

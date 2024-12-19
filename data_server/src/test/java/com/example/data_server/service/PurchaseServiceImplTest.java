package com.example.data_server.service;

import com.example.data_server.repository.*;
import com.example.data_server.service.PurchaseServiceImpl;
import com.example.shared.entities.domainEntities.*;
import com.example.shared.entities.userEntities.*;
import com.example.sep3.grpc.*;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
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
        // Mock Offer
        Offer offer = new Offer();
        offer.setId("offer123");
        offer.setNumberOfAvailableItems(10);

        // Mock Business
        Business business = new Business();
        business.setId("business123");

        offer.setBusiness(business);

        // Mock Customer
        Customer customer = new Customer();
        customer.setId("customer123");

        // Mock Purchase
        Purchase purchase = new Purchase();
        purchase.setId("purchase123");

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
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(purchase);

        // Execute
        purchaseServiceImpl.createPurchase(request, responseObserver);

        // Verify interactions
        verify(offerRepository).updateNumberOfAvailableItems("offer123", 5);
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

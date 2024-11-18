package com.example.data_server.service;

import com.example.data_server.repository.PurchaseRepository;
import com.example.data_server.utility.DateTimeConverter;
import com.example.sep3.grpc.*;
import com.example.shared.dao.PurchaseDao;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PurchaseServiceImplTest {

    @Mock
    private PurchaseRepository purchaseRepository;

    @InjectMocks
    private PurchaseServiceImpl purchaseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddPurchase() {
        PurchaseDao purchase = new PurchaseDao();
        purchase.setId("purchase123");
        purchase.setUserId("user123");
        purchase.setQuantity(2);
        purchase.setPurchaseDate(DateTimeConverter.getCurrentDateDao());

        when(purchaseRepository.save(any(PurchaseDao.class))).thenReturn(purchase);

        AddPurchaseRequest request = AddPurchaseRequest.newBuilder()
                .setUserId("user123")
                .setQuantity(2)
                .setPurchaseDate(DateTimeConverter.convertDateDaoToGoogleDate(purchase.getPurchaseDate()))
                .build();

        StreamObserver<AddPurchaseResponse> responseObserver = mock(StreamObserver.class);
        purchaseService.addPurchase(request, responseObserver);

        verify(responseObserver).onNext(any(AddPurchaseResponse.class));
        verify(responseObserver).onCompleted();
    }

    @Test
    void testGetPurchaseById() {
      PurchaseDao purchase = new PurchaseDao();
      purchase.setId("purchase123");
      purchase.setUserId("user123");
      purchase.setQuantity(2);
      purchase.setPurchaseDate(DateTimeConverter.getCurrentDateDao());

        when(purchaseRepository.findById("purchase123")).thenReturn(Optional.of(purchase));

        PurchaseIdRequest request = PurchaseIdRequest.newBuilder()
                .setId("purchase123")
                .build();

        StreamObserver<PurchaseResponse> responseObserver = mock(StreamObserver.class);
        purchaseService.getPurchaseById(request, responseObserver);

        verify(responseObserver).onNext(any(PurchaseResponse.class));
        verify(responseObserver).onCompleted();
    }

    @Test
    void testGetAllPurchases() {
      PurchaseDao purchase1 = new PurchaseDao();
      purchase1.setId("purchase123");
      purchase1.setUserId("user123");
      purchase1.setQuantity(2);
      purchase1.setPurchaseDate(DateTimeConverter.getCurrentDateDao());

        PurchaseDao purchase2 = new PurchaseDao();
        purchase2.setId("purchase456");
        purchase2.setUserId("user456");
        purchase2.setQuantity(3);
        purchase2.setPurchaseDate(DateTimeConverter.getCurrentDateDao());

        when(purchaseRepository.findAll()).thenReturn(
            List.of(purchase1, purchase2));

        StreamObserver<PurchaseList> responseObserver = mock(StreamObserver.class);
        purchaseService.getAllPurchases(Empty.newBuilder().build(), responseObserver);

        verify(responseObserver).onNext(any(PurchaseList.class));
        verify(responseObserver).onCompleted();
    }
}
package com.example.data_server.service;

import com.example.data_server.DataServerApplication;
import com.example.data_server.repository.PurchaseRepository;
import com.example.data_server.utility.DateTimeConverter;
import com.example.sep3.grpc.*;
import com.example.shared.dao.PurchaseDao;
import com.google.protobuf.Empty;
import io.grpc.internal.testing.StreamRecorder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = {DataServerApplication.class, PurchaseServiceImpl.class})
class PurchaseServiceImplTest {
  @Autowired
  private PurchaseRepository purchaseRepository;

  private PurchaseServiceImpl purchaseService;

  @BeforeEach
  public void setUp() {
    purchaseService = new PurchaseServiceImpl(purchaseRepository);
  }

  @Test
  public void testAddPurchase() throws Exception {
    AddPurchaseRequest request = AddPurchaseRequest.newBuilder()
        .setUserId("user123")
        .setQuantity(2)
        .setPurchaseDate(
            DateTimeConverter.convertDateDaoToGoogleDate(new PurchaseDao().getPurchaseDate()))
        .build();

    StreamRecorder<AddPurchaseResponse> responseObserver = StreamRecorder.create();
    purchaseService.addPurchase(request, responseObserver);
    responseObserver.awaitCompletion();

    AddPurchaseResponse response = responseObserver.getValues().get(0);
    Optional<PurchaseDao> purchase = purchaseRepository.findById(response.getId());

    assertThat(purchase).isPresent();
    assertThat(purchase.get().getUserId()).isEqualTo("user123");
    assertThat(purchase.get().getQuantity()).isEqualTo(2);
  }

  @Test
  public void testGetPurchaseById() throws Exception {
    PurchaseDao purchase = new PurchaseDao();
    purchase.setUserId("user123");
    purchase.setQuantity(2);
    purchase = purchaseRepository.save(purchase);

    PurchaseIdRequest request = PurchaseIdRequest.newBuilder()
        .setId(purchase.getId())
        .build();

    StreamRecorder<PurchaseResponse> responseObserver = StreamRecorder.create();
    purchaseService.getPurchaseById(request, responseObserver);
    responseObserver.awaitCompletion();

    PurchaseResponse response = responseObserver.getValues().get(0);

    assertThat(response.getUserId()).isEqualTo("user123");
    assertThat(response.getQuantity()).isEqualTo(2);
  }

  @Test
  public void testGetAllPurchases() throws Exception {
    PurchaseDao purchase1 = new PurchaseDao();
    purchase1.setUserId("user123");
    purchase1.setQuantity(2);
    purchaseRepository.save(purchase1);

    PurchaseDao purchase2 = new PurchaseDao();
    purchase2.setUserId("user456");
    purchase2.setQuantity(3);
    purchaseRepository.save(purchase2);

    StreamRecorder<PurchaseList> responseObserver = StreamRecorder.create();
    purchaseService.getAllPurchases(Empty.newBuilder().build(), responseObserver);
    responseObserver.awaitCompletion();

    PurchaseList response = responseObserver.getValues().get(0);

    assertThat(response.getPurchasesList()).hasSize(2);
  }

}
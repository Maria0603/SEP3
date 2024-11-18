package com.example.data_server.service;

import com.example.sep3.grpc.*;
import com.example.data_server.repository.PurchaseRepository;
import com.example.shared.dao.PurchaseDao;
import com.example.data_server.utility.DateTimeConverter;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@GrpcService
public class PurchaseServiceImpl extends PurchaseServiceGrpc.PurchaseServiceImplBase {

    private final PurchaseRepository purchaseRepository;

    @Autowired
    public PurchaseServiceImpl(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    public void addPurchase(AddPurchaseRequest request, StreamObserver<AddPurchaseResponse> responseObserver) {
        System.out.println("Request to add purchase");

        PurchaseDao purchase = new PurchaseDao();
        purchase.setUserId(request.getUserId());
        purchase.setQuantity(request.getQuantity());
        purchase.setPurchaseDate(DateTimeConverter.convertGoogleDateToDateDao(request.getPurchaseDate()));

        PurchaseDao createdPurchase = purchaseRepository.save(purchase);

        AddPurchaseResponse response = AddPurchaseResponse.newBuilder()
                .setId(createdPurchase.getId())
                .setUserId(createdPurchase.getUserId())
                .setQuantity(createdPurchase.getQuantity())
                .setPurchaseDate(DateTimeConverter.convertDateDaoToGoogleDate(createdPurchase.getPurchaseDate()))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getPurchaseById(PurchaseIdRequest request, StreamObserver<PurchaseResponse> responseObserver) {
        System.out.println("Request for purchase by id");

        Optional<PurchaseDao> purchase = purchaseRepository.findById(request.getId());
        if (purchase.isPresent()) {
            PurchaseDao purchaseDao = purchase.get();

            PurchaseResponse response = PurchaseResponse.newBuilder()
                    .setId(purchaseDao.getId())
                    .setOfferId(request.getId())
                    .setUserId(purchaseDao.getUserId())
                    .setQuantity(purchaseDao.getQuantity())
                    .setPurchaseDate(DateTimeConverter.convertDateDaoToGoogleDate(purchaseDao.getPurchaseDate()))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new Exception("Error: No purchase with ID " + request.getId()));
        }
    }

    @Override public void getAllPurchases(Empty request,
        StreamObserver<PurchaseList> responseObserver) {
        System.out.println("Request for all purchases");

        List<PurchaseDao> purchases = purchaseRepository.findAll();

        PurchaseList.Builder purchaseListBuilder = PurchaseList.newBuilder();
        for (PurchaseDao purchaseDao : purchases) {
            PurchaseResponse response = PurchaseResponse.newBuilder()
                .setId(purchaseDao.getId())
                .setUserId(purchaseDao.getUserId())
                .setQuantity(purchaseDao.getQuantity())
                .setPurchaseDate(DateTimeConverter.convertDateDaoToGoogleDate(purchaseDao.getPurchaseDate()))
                .build();
            purchaseListBuilder.addPurchases(response);
        }

        PurchaseList purchaseListResponse = purchaseListBuilder.build();
        responseObserver.onNext(purchaseListResponse);
        responseObserver.onCompleted();
    }


}
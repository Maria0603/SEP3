package com.example.data_server.service;

import com.example.data_server.repository.BusinessRepository;
import com.example.data_server.repository.CustomerRepository;
import com.example.data_server.repository.OfferRepository;
import com.example.data_server.repository.PurchaseRepository;
import com.example.sep3.grpc.*;
import com.example.shared.entities.domainEntities.Offer;
import com.example.shared.entities.domainEntities.Purchase;
import com.example.shared.entities.usersEntities.Business;
import com.example.shared.entities.usersEntities.Customer;
import com.example.shared.model.OfferStatus;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static com.example.data_server.converters.PurchaseEntityGrpcConverter.*;

@GrpcService public class PurchaseServiceImpl
    extends PurchaseServiceGrpc.PurchaseServiceImplBase {

  private final PurchaseRepository purchaseRepository;
  private final OfferRepository offerRepository;
  private final BusinessRepository businessRepository;
  private final CustomerRepository customerRepository;

  @Autowired public PurchaseServiceImpl(PurchaseRepository purchaseRepository,
      OfferRepository offerRepository, BusinessRepository businessRepository,
      CustomerRepository customerRepository) {
    this.purchaseRepository = purchaseRepository;
    this.offerRepository = offerRepository;
    this.businessRepository = businessRepository;
    this.customerRepository = customerRepository;
    System.out.println("PurchaseServiceImpl created");
  }

  @Override public void createPurchase(CreatePurchaseRequest request,
      StreamObserver<PurchaseResponse> responseObserver) {
    System.out.println("Request to add Purchase");

    Offer offer = offerRepository.findById(request.getOfferId()).get();

    //Extract business
    Optional<Business> businessOptional = businessRepository.findById(
        offer.getBusiness().getId());
    if (businessOptional.isEmpty())
      throw new IllegalArgumentException(
          "Business not found with ID: " + offer.getBusiness().getId());
    Business business = businessOptional.get();

    // Extract customer
    Optional<Customer> customerOptional = customerRepository.findById(
        request.getCustomerId());
    if (customerOptional.isEmpty())
      throw new IllegalArgumentException(
          "Customer not found with ID: " + request.getCustomerId());

    Customer customer = customerOptional.get();

    Purchase purchase = generatePurchaseFromCreatePurchaseRequest(request,
        offer, customer, business);

    if (offer.getNumberOfAvailableItems() == request.getNumberOfItems())
      offerRepository.updateStatus(offer.getId(),
          OfferStatus.RESERVED.getStatus());

    offerRepository.updateNumberOfAvailableItems(offer.getId(),
        offer.getNumberOfAvailableItems() - request.getNumberOfItems());

    Purchase createdPurchase = purchaseRepository.save(purchase);

    PurchaseResponse purchaseResponse = generatePurchaseResponseFromPurchase(
        createdPurchase);
    responseObserver.onNext(purchaseResponse);
    responseObserver.onCompleted();
  }

  @Override public void getPurchaseById(PurchaseIdRequest request,
      StreamObserver<PurchaseResponse> responseObserver) {
    System.out.println("Request for purchase by id");

    Optional<Purchase> purchaseOptional = purchaseRepository.findById(
        request.getId());
    if (purchaseOptional.isPresent()) {
      Purchase purchase = purchaseOptional.get();
      PurchaseResponse purchaseResponse = generatePurchaseResponseFromPurchase(
          purchase);
      responseObserver.onNext(purchaseResponse);
      responseObserver.onCompleted();
    }
    else {
      responseObserver.onError(
          new Exception("Error: No purchase with ID " + request.getId()));
    }
  }
  @Override public void getDetailedPurchaseById(PurchaseIdRequest request,
      StreamObserver<DetailedPurchaseResponse> responseObserver)
  {
    System.out.println("Request for detailed purchase purchase by id");

    Optional<Purchase> purchaseOptional = purchaseRepository.findById(
        request.getId());
    if (purchaseOptional.isPresent())
    {
      Purchase purchase = purchaseOptional.get();
      DetailedPurchaseResponse purchaseResponse = generateDetailedPurchaseResponseFromPurchase(
          purchase);
      responseObserver.onNext(purchaseResponse);
      responseObserver.onCompleted();
    }
    else
    {
      responseObserver.onError(
          new Exception("Error: No purchase with ID " + request.getId()));
    }
  }

  @Override public void getPurchases(GetPurchaseRequest request,
      StreamObserver<PurchaseListResponse> responseObserver) {
    System.out.println("Request for all Purchases");

    switch (request.getRole()) {
      case "CUSTOMER":
        getPurchasesByCustomerId(request, responseObserver);
        break;
      case "BUSINESS":
        getPurchasesByBusinessId(request, responseObserver);
        break;
      default:
        getAllPurchases(responseObserver);
    }

  }

  @Override public void updatePurchaseStatus(
      PurchaseStatusRequest purchaseStatusRequest,
      StreamObserver<PurchaseResponse> responseObserver) {
    System.out.println("Request to update purchase status.");

    Purchase purchase = purchaseRepository.findById(
            purchaseStatusRequest.getId())
        .orElseThrow(() -> new IllegalArgumentException("purchase not found"));

    purchase.setStatus(purchaseStatusRequest.getStatus());
    purchaseRepository.save(purchase);

    PurchaseResponse purchaseResponse = generatePurchaseResponseFromPurchase(
        purchase);
    responseObserver.onNext(purchaseResponse);
    responseObserver.onCompleted();
  }

  private void getPurchasesByCustomerId(GetPurchaseRequest request,
      StreamObserver<PurchaseListResponse> responseObserver) {
    System.out.println("Request for all Purchases by customer id");

    List<Purchase> purchases = purchaseRepository.findByCustomerId(
        request.getUserId());
    PurchaseListResponse response = generatePurchaseListResponseFromPurchaseList(
        purchases);

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  private void getPurchasesByBusinessId(GetPurchaseRequest request,
      StreamObserver<PurchaseListResponse> responseObserver) {
    System.out.println("Request for all Purchases by business id");

    List<Purchase> purchases = purchaseRepository.findByBusinessId(
        request.getUserId());
    PurchaseListResponse response = generatePurchaseListResponseFromPurchaseList(
        purchases);

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  private void getAllPurchases(
      StreamObserver<PurchaseListResponse> responseObserver) {
    System.out.println("Request for all Purchases");

    List<Purchase> purchases = purchaseRepository.findAll();
    PurchaseListResponse response = generatePurchaseListResponseFromPurchaseList(
        purchases);

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

}
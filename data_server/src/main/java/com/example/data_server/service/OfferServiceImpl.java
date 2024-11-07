package com.example.data_server.service;

import com.example.sep3.grpc.*;
import com.example.shared.dao.OfferDao;
import com.example.shared.model.Status;
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.data_server.repository.OfferRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service public class OfferServiceImpl
    extends OfferServiceGrpc.OfferServiceImplBase
{
  @Autowired private OfferRepository offerRepository;
  private final String uploadDir = "../images";

  /*
  @Autowired public OfferServiceImpl(OfferRepository offerRepository)
  {
    this.offerRepository = offerRepository;
  }*/

  @Override public void getAllOffers(EmptyMessage request,
      StreamObserver<OfferList> responseObserver)
  {
    List<OfferDao> availableOffers = offerRepository.findByStatus(
        Status.AVAILABLE.getStatus());
    OfferList.Builder offerListBuilder = OfferList.newBuilder();
    for (OfferDao offerDao : availableOffers)
    {

      OfferResponse offerResponse = OfferResponse.newBuilder()
          .setId(offerDao.getId()).setTitle(offerDao.getTitle())
          .setDescription(offerDao.getDescription())
          .setStatus(offerDao.getStatus()).setPrice(offerDao.getPrice())
          .setNumberOfFoodBags(offerDao.getNumberOfFoodBags())
          .setPickupDate(offerDao.getPickupDate())
          .setPickupTimeStart(offerDao.getPickupTimeStart())
          .setPickupTimeEnd(offerDao.getPickupTimeEnd())
          .setImage(ByteString.copyFrom(extractImage(offerDao.getImagePath())))
          .addAllCategories(offerDao.getCategories()).build();

      offerListBuilder.addOffer(offerResponse);
    }

    OfferList offerListResponse = offerListBuilder.build();
    responseObserver.onNext(offerListResponse);
    responseObserver.onCompleted();
  }

  @Override public void getOfferById(OfferIdRequest request,
      StreamObserver<OfferResponse> responseObserver)
  {
    Optional<OfferDao> offer = offerRepository.findById(request.getId());
    if (offer.isPresent())
    {
      OfferDao offerDao = offer.get();
      OfferResponse offerResponse = OfferResponse.newBuilder()
          .setId(offerDao.getId()).setTitle(offerDao.getTitle())
          .setDescription(offerDao.getDescription())
          .setStatus(offerDao.getStatus()).setPrice(offerDao.getPrice())
          .setNumberOfFoodBags(offerDao.getNumberOfFoodBags())
          .setPickupDate(offerDao.getPickupDate())
          .setPickupTimeStart(offerDao.getPickupTimeStart())
          .setPickupTimeEnd(offerDao.getPickupTimeEnd())
          .setImage(ByteString.copyFrom(extractImage(offerDao.getImagePath())))
          .addAllCategories(offerDao.getCategories()).build();

      responseObserver.onNext(offerResponse);
      responseObserver.onCompleted();
    }

    //throw exception or send http status code
  }

  @Override public void saveOffer(SaveOfferRequest request,
      StreamObserver<SaveOfferResponse> responseObserver)
  {
    ArrayList<String> categories = new ArrayList<>(request.getCategoriesList());

    //create the date and time as grpc Time and Date messages
    Date pickupDate = Date.newBuilder()
        .setYear(request.getPickupDate().getYear())
        .setMonth(request.getPickupDate().getMonth())
        .setDay(request.getPickupDate().getDay()).build();
    Time pickupTimeStart = Time.newBuilder()
        .setHour(request.getPickupTimeStart().getHour())
        .setMinute(request.getPickupTimeStart().getMinute()).build();
    Time pickupTimeEnd = Time.newBuilder()
        .setHour(request.getPickupTimeEnd().getHour())
        .setMinute(request.getPickupTimeEnd().getMinute()).build();

    //The offer has no id or image for now
    OfferDao offer = new OfferDao();

    offer.setTitle(request.getTitle());
    offer.setDescription(request.getDescription());
    offer.setPrice(request.getPrice());
    offer.setPickupDate(pickupDate);
    offer.setPickupTimeStart(pickupTimeStart);
    offer.setPickupTimeEnd(pickupTimeEnd);
    offer.setCategories(categories);
    offer.setNumberOfFoodBags(request.getNumberOfFoodBags());

    //We save the offer in the database to extract the id
    OfferDao createdOffer = offerRepository.save(offer);

    //The image will be in the file system as {id}.jpg
    String offerId = createdOffer.getId();
    String imagePath = saveImage(request.getImage().toByteArray(), offerId);

    //we update the previously null image
    createdOffer.setImagePath(imagePath);
    offerRepository.save(createdOffer);

    //We build the response with everything
    SaveOfferResponse response = SaveOfferResponse.newBuilder()
        .setId(createdOffer.getId()).setTitle(createdOffer.getTitle())
        .setDescription(createdOffer.getDescription())
        .setPrice(createdOffer.getPrice())
        .setNumberOfFoodBags(createdOffer.getNumberOfFoodBags())
        .setPickupDate(createdOffer.getPickupDate())
        .setPickupTimeStart(createdOffer.getPickupTimeStart())
        .setPickupTimeEnd(createdOffer.getPickupTimeEnd())
        .setImage(request.getImage())
        .addAllCategories(createdOffer.getCategories()).build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  private String saveImage(byte[] imageBytes, String offerId)
  {
    String imageDir = "images";
    String imagePath = imageDir + "/" + offerId + ".jpg";
    File imageFile = new File(imagePath);
    try (FileOutputStream fos = new FileOutputStream(imageFile))
    {
      fos.write(imageBytes);
    }
    catch (IOException e)
    {
      e.printStackTrace();
      /*responseStreamObserver.onError(
          io.grpc.Status.INTERNAL.withDescription("Error processing image.")
              .asRuntimeException());*/
    }
    return imagePath;
  }

  private byte[] extractImage(String imagePath)
  {
    Path filePath = Paths.get(uploadDir, imagePath).normalize();
    byte[] imageBytes = null;
    try
    {
      imageBytes = Files.readAllBytes(filePath);
    }
    catch (IOException e)
    {
      e.printStackTrace();
      /*responseObserver.onError(new Throwable(
          String.valueOf(ResponseEntity.status(HttpStatus.NOT_FOUND))));*/
    }
    return imageBytes;
  }

}

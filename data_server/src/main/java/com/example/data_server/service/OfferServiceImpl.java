package com.example.data_server.service;

import com.example.data_server.utility.DateTimeConverter;
import com.example.sep3.grpc.*;
import com.example.shared.dao.OfferDao;
import com.example.shared.model.OfferStatus;
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.data_server.repository.OfferRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@GrpcService public class OfferServiceImpl
    extends OfferServiceGrpc.OfferServiceImplBase
{
  private OfferRepository offerRepository;
  private final String uploadDirectory = "data_server/images/";

  @Autowired public OfferServiceImpl(OfferRepository offerRepository)
  {
    this.offerRepository = offerRepository;
    System.out.println("OfferServiceImpl created");
  }

  /*
  message SaveOfferRequest {
  string title = 1;  string description = 2;  int32 originalPrice = 3;  int32 offerPrice = 4;
  int32 numberOfItems = 5;  Date pickupDate = 6;  Time pickupTimeStart = 7;
  Time pickupTimeEnd = 8;  repeated string categories = 9;   bytes image = 10;

  TODO for Matej: The data_server is expecting the image as an array of bytes
}
   */
  @Override public void saveOffer(SaveOfferRequest request,
      StreamObserver<SaveOfferResponse> responseObserver)
  {
    System.out.println("Request for save offer");

    ArrayList<String> categories = new ArrayList<>(request.getCategoriesList());

    //The offer has no id or image for now
    OfferDao offer = new OfferDao();

    offer.setTitle(request.getTitle());
    offer.setDescription(request.getDescription());
    offer.setOfferPrice(request.getOfferPrice());
    offer.setOriginalPrice(request.getOriginalPrice());

    offer.setPickupDate(
        DateTimeConverter.convertGrpcDateToDateDao(request.getPickupDate()));
    offer.setPickupTimeStart(DateTimeConverter.convertGrpcTimeToTimeDao(
        request.getPickupTimeStart()));
    offer.setPickupTimeEnd(
        DateTimeConverter.convertGrpcTimeToTimeDao(request.getPickupTimeEnd()));
    offer.setCategories(categories);
    offer.setNumberOfItems(request.getNumberOfItems());
    offer.setStatus(OfferStatus.AVAILABLE.getStatus());

    //We save the offer in the database to extract the id
    OfferDao createdOffer = offerRepository.save(offer);

    //The image will be in the file system as {id}.jpg
    String offerId = createdOffer.getId();

    //TODO: delete this part when working with the image, otherwise you will have a bunch of red images
    //<------- for testing purposes
    String imagePath = null;
    try
    {
      imagePath = saveImage(createImageByteArray(), offerId);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    // ----------------------->
    //correct line:
    //String imagePath = saveImage(request.getImage().toByteArray(), offerId);

    //we update the previously null image
    createdOffer.setImagePath(imagePath);
    offerRepository.save(createdOffer);

    //We build the response with everything
    SaveOfferResponse response = SaveOfferResponse.newBuilder()
        .setId(createdOffer.getId()).setTitle(createdOffer.getTitle())
        .setDescription(createdOffer.getDescription())
        .setOfferPrice(createdOffer.getOfferPrice())
        .setOriginalPrice(createdOffer.getOriginalPrice())
        .setNumberOfItems(createdOffer.getNumberOfItems()).setPickupDate(
            DateTimeConverter.convertDateDaoToGrpcDate(
                createdOffer.getPickupDate())).setPickupTimeStart(
            DateTimeConverter.convertTimeDaoToGrpcTime(
                createdOffer.getPickupTimeStart())).setPickupTimeEnd(
            DateTimeConverter.convertTimeDaoToGrpcTime(
                createdOffer.getPickupTimeEnd())).setImage(request.getImage())
        .addAllCategories(createdOffer.getCategories()).build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override public void getAvailableOffers(EmptyMessage request,
      StreamObserver<OfferList> responseObserver)
  {
    System.out.println("Request for all offers");
    List<OfferDao> availableOffers = offerRepository.findByStatus(
        OfferStatus.AVAILABLE.getStatus());

    OfferList.Builder offerListBuilder = OfferList.newBuilder();
    for (OfferDao offerDao : availableOffers)
      offerListBuilder.addOffer(
          buildOfferResponse(offerDao)); //method to build the response

    OfferList offerListResponse = offerListBuilder.build();
    responseObserver.onNext(offerListResponse);
    responseObserver.onCompleted();
  }

  @Override public void getOfferById(OfferIdRequest request,
      StreamObserver<OfferResponse> responseObserver)
  {
    System.out.println("Request for offer by id");

    Optional<OfferDao> offer = offerRepository.findById(request.getId());
    if (offer.isPresent())
    {
      OfferDao offerDao = offer.get();

      OfferResponse offerResponse = buildOfferResponse(
          offerDao); //method to build the response

      responseObserver.onNext(offerResponse);
      responseObserver.onCompleted();
    }

    responseObserver.onError(
        new Exception("Error: No offer with ID " + request.getId()));
  }

  private OfferResponse buildOfferResponse(OfferDao offerDao)
  {
    return OfferResponse.newBuilder().setId(offerDao.getId())
        .setTitle(offerDao.getTitle()).setDescription(offerDao.getDescription())
        .setStatus(offerDao.getStatus()).setOfferPrice(offerDao.getOfferPrice())
        .setOriginalPrice(offerDao.getOriginalPrice())
        .setNumberOfItems(offerDao.getNumberOfItems()).setPickupDate(
            DateTimeConverter.convertDateDaoToGrpcDate(
                offerDao.getPickupDate())).setPickupTimeStart(
            DateTimeConverter.convertTimeDaoToGrpcTime(
                offerDao.getPickupTimeStart())).setPickupTimeEnd(
            DateTimeConverter.convertTimeDaoToGrpcTime(
                offerDao.getPickupTimeEnd()))
        .setImage(ByteString.copyFrom(extractImage(offerDao.getImagePath())))
        .addAllCategories(offerDao.getCategories()).build();
  }

  private String saveImage(byte[] imageBytes, String offerId)
  {
    String pathToImage = null;
    try
    {
      ByteArrayInputStream inStreamObj = new ByteArrayInputStream(imageBytes);
      BufferedImage newImage = ImageIO.read(inStreamObj);

      pathToImage = uploadDirectory + offerId + ".jpg";
      ImageIO.write(newImage, "jpg", new File(pathToImage));
    }
    catch (IOException e)
    {
      e.printStackTrace();
       /*responseStreamObserver.onError(
          io.grpc.OfferStatus.INTERNAL.withDescription("Error processing image.")
              .asRuntimeException());*/
    }
    return pathToImage;
  }

  //Method to create a dummy red image, for testing purposes; do not delete
  private byte[] createImageByteArray() throws IOException
  {
    // Create a 200x200 BufferedImage with RGB color
    BufferedImage bufferedImage = new BufferedImage(200, 200,
        BufferedImage.TYPE_INT_RGB);

    // Fill the image with a solid color
    for (int y = 0; y < 200; y++)
      for (int x = 0; x < 200; x++)
        bufferedImage.setRGB(x, y, (255 << 16) | (0 << 8) | 0); // Red

    // Convert BufferedImage to byte array in JPEG format
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(bufferedImage, "jpg", baos);
    return baos.toByteArray();
  }

  private byte[] extractImage(String imagePath)
  {
    try
    {
      BufferedImage image = ImageIO.read(new File(imagePath));
      ByteArrayOutputStream outStreamObj = new ByteArrayOutputStream();
      ImageIO.write(image, "jpg", outStreamObj);
      return outStreamObj.toByteArray();
    }
    catch (IOException e)
    {
      e.printStackTrace();
       /*responseStreamObserver.onError(
          io.grpc.OfferStatus.INTERNAL.withDescription("Error processing image.")
              .asRuntimeException());*/
    }
    return null;
  }



}

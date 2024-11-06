package service;

import com.example.sep3.grpc.*;
import com.example.shared.dao.DateDao;
import com.example.shared.dao.OfferDao;
import com.example.shared.dao.TimeDao;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.OfferRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service public class OfferServiceImpl
    extends OfferServiceGrpc.OfferServiceImplBase
{
  private final OfferRepository offerRepository;

  private final String uploadDir = "../images";

  @Autowired public OfferServiceImpl(OfferRepository offerRepository)
  {
    this.offerRepository = offerRepository;
  }

  public List<OfferDao> getAllOffers()
  {
    return offerRepository.findAll();
  }

  public Optional<OfferDao> getOfferById(String id)
  {
    return offerRepository.findById(id);
  }

  public void createOffer(SaveOfferRequest request,
      StreamObserver<SaveOfferResponse> responseStreamObserver)
  {
    String[] categories = new String[request.getCategoriesCount()];
    for (int i = 0; i < request.getCategoriesCount(); i++)
      categories[i] = request.getCategories(i);

    //create the date and time as grpc Time and Date messages
    Date pickupDate = Date.newBuilder().setYear(request.getPickupDate().getYear()).setMonth(request.getPickupDate().getMonth()).setDay(request.getPickupDate().getDay()).build();
    Time pickupTimeStart = Time.newBuilder().setHour(request.getPickupTimeStart().getHour()).setMinute(request.getPickupTimeStart().getMinute()).build();
    Time pickupTimeEnd = Time.newBuilder().setHour(request.getPickupTimeEnd().getHour()).setMinute(request.getPickupTimeEnd().getMinute()).build();

    //The offer has no id or image for now
    OfferDao offer = new OfferDao(null, request.getTitle(),
        request.getDescription(), request.getPrice(), pickupDate,
        pickupTimeStart, pickupTimeEnd, categories,
        request.getNumberOfFoodBags(), null);

    //We save it in the database to extract the id
    OfferDao createdOffer = offerRepository.save(offer);

    //The image will be in the file system as {id}.jpg
    String fileName = createdOffer.getId() + ".jpg";
    Path filePath = Paths.get(uploadDir, fileName);

    try
    {
      Files.write(filePath, request.getImage().toByteArray());
    }
    catch (IOException e)
    {
      System.out.println("Error processing image    " + e.getMessage());
      responseStreamObserver.onError(
          io.grpc.Status.INTERNAL.withDescription("Error processing image.")
              .asRuntimeException());
    }

    String imagePath = "/api/images/" + fileName;

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
        .setPickupTimeEnd(createdOffer.getPickupTimeEnd()).setImagePath(request.getImage())
        .addAllCategories(Arrays.asList(createdOffer.getCategories())).build();

    responseStreamObserver.onNext(response);
    responseStreamObserver.onCompleted();
  }

}

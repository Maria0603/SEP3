package com.example.server.services;

import com.example.sep3.grpc.*;
import com.example.server.DataServerStub;
import com.example.server.dto.CreateOfferRequestDto;
import com.example.server.converters.DateConverter;
import com.example.server.converters.TimeConverter;
import com.example.server.dto.DateDto;
import com.example.server.dto.TimeDto;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.example.server.converters.DtoGrpcConverter.CreateOfferRequestDto_To_SaveOfferRequest;

@Service public class OfferService extends OfferServiceGrpc.OfferServiceImplBase
{
  private final DataServerStub dataServerStub;

  @Autowired public OfferService(DataServerStub dataServerStub)
  {
    this.dataServerStub = dataServerStub;
    System.out.println("OfferService created");
  }
  @Override
  public void getAvailableOffers(EmptyMessage request, StreamObserver<OfferList> responseObserver) {
    System.out.println("SERVER: getAvailableOffers called");

    OfferList.Builder offerListBuilder = OfferList.newBuilder();
    // Mock example; replace with actual fetching logic
    offerListBuilder.addOffer(
            OfferResponse.newBuilder()
                    .setId("1")
                    .setTitle("Sample Offer")
                    .setDescription("A great offer!")
                    .setOriginalPrice(100)
                    .setOfferPrice(80)
                    .setNumberOfItems(10)
                    .setStatus("AVAILABLE")
                    .build()
    );
    offerListBuilder.addOffer(
            OfferResponse.newBuilder()
                    .setId("2")
                    .setTitle("Second Sample Offer")
                    .setDescription("A Amazing offer!")
                    .setOriginalPrice(100)
                    .setOfferPrice(80)
                    .setNumberOfItems(10)
                    .setStatus("AVAILABLE")
                    .build()
    );

    responseObserver.onNext(offerListBuilder.build());
    responseObserver.onCompleted();
  }
  public String saveOffer(CreateOfferRequestDto offerRequestDto)
  {
    //First check what we couldn't check in the Dto class
    if(!isPickupInFuture(offerRequestDto.getPickupDate(), offerRequestDto.getPickupTimeEnd()))
      throw new IllegalArgumentException("Pickup date and time must be in the future");


    //Transform the dto to grpc message
    SaveOfferRequest request = CreateOfferRequestDto_To_SaveOfferRequest(
        offerRequestDto);

    //Send the request to the data server
    SaveOfferResponse response = dataServerStub.saveOffer(request);

    //return the id; maybe more?
    return response.getId();
  }

  private boolean isPickupInFuture(DateDto date, TimeDto time)
  {
    LocalDateTime pickupDateTime = LocalDateTime.of(
        LocalDate.of(date.getYear(), date.getMonth(), date.getDay()),
        LocalTime.of(time.getHour(), time.getMinute()));
    return pickupDateTime.isAfter(LocalDateTime.now());
  }

}

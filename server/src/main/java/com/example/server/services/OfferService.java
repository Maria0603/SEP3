package com.example.server.services;

import com.example.sep3.grpc.*;
import com.example.server.DataServerStub;
import com.example.server.dto.CreateOfferRequestDto;
import com.example.server.converters.DateConverter;
import com.example.server.converters.TimeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.server.converters.DtoGrpcConverter.CreateOfferRequestDto_To_SaveOfferRequest;

@Service public class OfferService extends OfferServiceGrpc.OfferServiceImplBase
{
  private final DataServerStub dataServerStub;

  @Autowired public OfferService(DataServerStub dataServerStub)
  {
    this.dataServerStub = dataServerStub;
    System.out.println("OfferService created");
  }

  public String saveOffer(CreateOfferRequestDto offerRequestDto)
  {
    //Transform the dto to grpc message
    SaveOfferRequest request = CreateOfferRequestDto_To_SaveOfferRequest(offerRequestDto);

    //Send the request to the data server
    SaveOfferResponse response = dataServerStub.saveOffer(request);

    //return the id; maybe more?
    return response.getId();
  }


}

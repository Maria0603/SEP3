package com.example.server;

import com.example.sep3.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

 public class GrpcClient
{
  private final ManagedChannel channel;
  private final OfferServiceGrpc.OfferServiceBlockingStub blockingStub;

  public GrpcClient(String host, int port)
  {
    channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext()
        .usePlaintext().build();

    blockingStub=OfferServiceGrpc.newBlockingStub(channel);
  }


  public OfferList getAllAvailableOffers(EmptyMessage request)
  {
    return blockingStub.getAllOffers(request);
  }

  public SaveOfferResponse saveOffer(SaveOfferRequest request)
  {
    return blockingStub.saveOffer(request);
  }

  public OfferResponse getOfferById(OfferIdRequest request)
  {
    return blockingStub.getOfferById(request);
  }

  public void shutdown()
  {
    channel.shutdown();
  }
}

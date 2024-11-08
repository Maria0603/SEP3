package com.example.server.Services.OfferService;

import com.example.sep3.grpc.EmptyMessage;
import com.example.sep3.grpc.OfferList;
import com.example.sep3.grpc.OfferServiceGrpc;
import com.example.server.GrpcClient;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service public class OfferServiceImpl
    extends OfferServiceGrpc.OfferServiceImplBase
{
  private final GrpcClient client;

  @Autowired
  public OfferServiceImpl(GrpcClient grpcClient)
  {
    this.client = grpcClient;
    System.out.println("OfferServiceImpl created");
  }

  public OfferList getAllAvailableOffers()
  {
    EmptyMessage request = EmptyMessage.newBuilder().build();
    return client.getAllAvailableOffers(request);
  }

}

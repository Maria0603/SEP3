package com.example.server.Client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import proto.CollectServiceGrpc.*;
import proto.FoodBag.*;
import static proto.CollectServiceGrpc.newBlockingStub;

public class TestClient
{
  public static void main(String[] args)
  {

    ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(
        "localhost", 50051).usePlaintext().build();

    CollectServiceBlockingStub stub = newBlockingStub(
        managedChannel);

    EmptyMessage request = EmptyMessage.newBuilder().build();

    FoodBagList response = stub.getFoodBags(request);

    System.out.println(
        "***************************************************************");
    System.out.println("Received response: " + response.getFoodBagsList());
    System.out.println(
        "===============================================================");

    managedChannel.shutdown();
  }
}



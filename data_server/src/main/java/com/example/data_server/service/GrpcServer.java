package com.example.data_server.service;

import com.example.data_server.repository.OfferRepository;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GrpcServer
{
  private Server server;

  private final OfferRepository offerRepository;

  public GrpcServer(OfferRepository offerRepository)
  {
    this.offerRepository = offerRepository;
  }

  public void start() throws Exception
  {
    server = ServerBuilder.forPort(8081).addService(new OfferServiceImpl(offerRepository)).build().start();
    System.out.println("Data Server started, listening on " + server.getPort());

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      System.err.println("Shutting down since JVM is shutting down.");
      GrpcServer.this.stop();
    }));
    server.awaitTermination();
  }

  public void stop()
  {
    if(server != null)
      server.shutdown();
  }
}

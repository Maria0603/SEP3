package com.example.data_server.service;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.stereotype.Service;

@Service
public class GrpcServer
{
  private Server server;

  public void start() throws Exception
  {
    server = ServerBuilder.forPort(9090).addService(new OfferServiceImpl()).build().start();
    System.out.println("Server started, listening on " + server.getPort());

    Runtime.getRuntime().addShutdownHook(new Thread(()-> {
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

package com.example.server.config;

import jakarta.websocket.RemoteEndpoint;
import net.devh.boot.grpc.server.security.authentication.BasicGrpcAuthenticationReader;
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration public class GrpcSecurityConfig
{

  @Bean public GrpcAuthenticationReader grpcAuthenticationReader()
  {
    return new BasicGrpcAuthenticationReader();
  }
}


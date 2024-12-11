package com.example.server.config;

import com.example.server.services.auxServices.Implementations.WebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.WebSocketSession;

@Configuration @EnableWebSocket // Enables WebSocket support in Spring Boot
public class WebSocketConfig implements WebSocketConfigurer
{

  // Registers a custom WebSocket handler and endpoint
  @Override public void registerWebSocketHandlers(
      WebSocketHandlerRegistry registry)
  {
    // Register the WebSocket handler for the '/ws' endpoint
    registry.addHandler(new WebSocketHandler(), "/ws/notifications")
        .setAllowedOrigins("*"); // Allows connections from any origin
  }

  // Bean to initialize WebSocket support with a container
  @Bean public ServerEndpointExporter serverEndpointExporter()
  {
    return new ServerEndpointExporter(); // Allows WebSocket server to be exported
  }
}

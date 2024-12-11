package com.example.server.services.auxServices.Implementations;

import com.example.server.security.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class WebSocketHandler extends TextWebSocketHandler
{
  @Autowired private JWTUtils jwtUtils;
  private final Map<String, WebSocketSession> sessionMap = new HashMap<>();
  private final Map<String, String> userTopics = new HashMap<>(); // Maps user to their unique topic

  // Handle new WebSocket connections
  @Override public void afterConnectionEstablished(WebSocketSession session)
      throws Exception
  {
    System.out.println("Connection established");
    // Get authentication token from headers
    String authHeader = (String) session.getAttributes().get("Authorization");

    // Extract userId and userRole from JWT or session (mocked here)
    String userId = extractUserIdFromToken(authHeader);
    String userRole = extractUserRoleFromToken(authHeader);
    System.out.println("User ID: " + userId + " Role: " + userRole);

    if (userId != null && userRole != null)
    {
      String topic = "/topic/notifications/" + userRole + "/"
          + userId;  // Unique topic per user
      userTopics.put(session.getId(), topic);  // Store the user's unique topic
      sessionMap.put(session.getId(),
          session);  // Store the session for later use

      System.out.println("User connected: " + userRole + "/" + userId);
    }
  }

  // Handle incoming WebSocket messages
  @Override protected void handleTextMessage(WebSocketSession session,
      TextMessage message) throws IOException
  {
    // Process the received message (this example simply prints it)
    System.out.println("Message received: " + message.getPayload());
  }

  // Send a notification to a user-specific channel
  public void sendNotification(String userId, String userRole,
      String messageContent)
  {
    // Construct the topic/channel to send the message to
    String topic = "/topic/notifications/" + userRole + "/" + userId;

    // Find the WebSocket session associated with the topic
    WebSocketSession session = sessionMap.values().stream()
        .filter(s -> topic.equals(userTopics.get(s.getId()))).findFirst()
        .orElse(null);  // If no session is found, return null

    if (session != null && session.isOpen())
    {
      try
      {
        session.sendMessage(new TextMessage(
            messageContent));  // Send the message to the user's session
        System.out.println("Notification sent to: " + topic);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
  }

  // Cleanup after connection is closed
  @Override public void afterConnectionClosed(WebSocketSession session,
      org.springframework.web.socket.CloseStatus status) throws Exception
  {
    // Remove the session and topic when connection is closed
    String topic = userTopics.remove(session.getId());
    sessionMap.remove(session.getId());

    System.out.println("User disconnected: " + topic);
  }

  // Example methods to extract user information from the token
  private String extractUserIdFromToken(String authHeader)
  {
    return jwtUtils.extractUserId(authHeader);
  }

  private String extractUserRoleFromToken(String authHeader)
  {
    return jwtUtils.extractRoles(authHeader).getFirst();
  }
}
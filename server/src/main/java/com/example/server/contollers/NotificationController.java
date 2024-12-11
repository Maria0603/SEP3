package com.example.server.contollers;

import com.example.server.dto.notifications.NotificationResponseDto;
import com.example.server.security.JWTAuthFilter;
import com.example.server.security.JWTUtils;
import com.example.server.services.INotificationService;
import com.example.server.services.Implementations.NotificationService;
import com.example.server.services.Implementations.OfferService;
import com.example.server.services.auxServices.Implementations.EventService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@RestController @RequestMapping("/notifications") public class NotificationController
{

  private final INotificationService notificationService;
  private final EventService eventService;
  private JWTUtils jwtUtils;

  @Autowired public NotificationController(
      INotificationService notificationService, JWTUtils jwtUtils,
      EventService eventService)
  {
    this.notificationService = notificationService;
    this.eventService = eventService;
    this.jwtUtils = jwtUtils;
  }

  @GetMapping public ResponseEntity<List<NotificationResponseDto>> getNotifications(
      HttpServletRequest request)
  {
    String userId = (String) request.getAttribute("userId");
    String authHeader = request.getHeader("Authorization");
    String token = authHeader.substring(7);

    String userRole = jwtUtils.extractRoles(token).getFirst();
    System.out.println("Roleeeeeeeeeeeee: " + userRole);
    return ResponseEntity.ok(
        notificationService.getNotifications(userId, userRole));
  }

 /* @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE) public Flux<String> streamNotifications()
  {
    return Flux.interval(Duration.ofSeconds(1))
        .map(sequence -> "Notification " + sequence + " at " + Instant.now());
  }*/

  @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE) public SseEmitter streamNotifications(
      @RequestParam String token)
  {
    if(!jwtUtils.isTokenExpired(token))
    {
      String userId = jwtUtils.extractUserId(token);
      String userRole = jwtUtils.extractRoles(token).getFirst();
      String user = userRole + userId;
      System.out.println("Useeeeeeeeeer: " + user);

      return eventService.connectUser(user);
    }
    return null;
  }
}


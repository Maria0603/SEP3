package com.example.server.contollers;

import com.example.server.dto.notifications.NotificationResponseDto;
import com.example.server.security.JWTAuthFilter;
import com.example.server.security.JWTUtils;
import com.example.server.services.INotificationService;
import com.example.server.services.Implementations.NotificationService;
import com.example.server.services.Implementations.OfferService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/notifications") public class NotificationController
{

  private final INotificationService notificationService;
  private JWTUtils jwtUtils;

  @Autowired public NotificationController(
      INotificationService notificationService, JWTUtils jwtUtils)
  {
    this.notificationService = notificationService;
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
    return ResponseEntity.ok(notificationService.getNotifications(userId, userRole));
  }
}


package com.example.server.contollers;

import com.example.server.dto.notifications.NotificationResponseDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller public class WebSocketNotificationController
{

  @MessageMapping("/send") @SendTo("/topic/notifications") public NotificationResponseDto send(
      NotificationResponseDto notification)
  {
    return notification;
  }
}


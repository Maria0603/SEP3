package com.example.server.services.Implementations;

import com.example.sep3.grpc.NotificationsRequest;
import com.example.server.DataServerStub;
import com.example.server.converters.NotificationDtoGrpcConverter;
import com.example.server.dto.notifications.NotificationResponseDto;
import com.example.server.services.INotificationService;
import com.example.server.services.auxServices.Implementations.ImageStorageStorageService;
import com.example.server.services.auxServices.Implementations.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service public class NotificationService implements INotificationService
{
  private final DataServerStub dataServerStub;
  private final WebSocketHandler webSocketHandler;

  @Autowired public NotificationService(DataServerStub dataServerStub,
      WebSocketHandler webSocketHandler)
  {
    this.dataServerStub = dataServerStub;
    this.webSocketHandler = webSocketHandler;
    System.out.println("NotificationService created");
  }

  @Override public List<NotificationResponseDto> getNotifications(String userId,
      String userRole)
  {
    return NotificationDtoGrpcConverter.NotificationListResponse_To_ListNotificationResponseDto(
        dataServerStub.getNotifications(
            NotificationsRequest.newBuilder().setUserId(userId)
                .setUserRole(userRole).build()));
  }

  @Override public void sendNotification(NotificationResponseDto notification)
  {
    String userRole = notification.getUserRole();
    String userId = notification.getUserId();
    String notificationMessage = notification.getContent();

    // Send the notification to the specific user's topic
    webSocketHandler.sendNotification(userId, userRole, notificationMessage);
  }

}

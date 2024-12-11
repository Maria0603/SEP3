package com.example.server.services.Implementations;

import com.example.sep3.grpc.NotificationsRequest;
import com.example.server.DataServerStub;
import com.example.server.converters.NotificationDtoGrpcConverter;
import com.example.server.dto.notifications.NotificationResponseDto;
import com.example.server.services.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service public class NotificationService implements INotificationService
{
  private final DataServerStub dataServerStub;

  @Autowired public NotificationService(DataServerStub dataServerStub)
  {
    this.dataServerStub = dataServerStub;
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

}

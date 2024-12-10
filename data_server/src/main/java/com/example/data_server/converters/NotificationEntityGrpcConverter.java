package com.example.data_server.converters;

import com.example.sep3.grpc.NotificationListResponse;
import com.example.sep3.grpc.NotificationRequestResponse;
import com.example.shared.converters.DateTimeConverter;
import com.example.shared.entities.domainEntities.Notification;

public class NotificationEntityGrpcConverter
{
  public static Notification generateNotificationFromNotificationRequestResponse(
      NotificationRequestResponse grpc)
  {
    Notification notification = new Notification();
    notification.setUserId(grpc.getUserId());
    notification.setUserRole(grpc.getUserRole());

    notification.setSubjectId(grpc.getSubjectId());

    notification.setContent(grpc.getContent());
    notification.setType(grpc.getType());
    notification.setTimestamp(
        DateTimeConverter.convertProtoTimestamp_To_LocalDateTime(
            grpc.getTimestamp()));

    return notification;
  }

  public static NotificationRequestResponse generateNotificationRequestResponseFromNotification(
      Notification notification)
  {
    return NotificationRequestResponse.newBuilder()
        .setUserId(notification.getUserId())
        .setSubjectId(notification.getSubjectId())
        .setContent(notification.getContent()).setTimestamp(
            DateTimeConverter.convertLocalDateTime_To_ProtoTimestamp(
                notification.getTimestamp())).setType(notification.getType())
        .setUserRole(notification.getUserRole()).build();
  }

}

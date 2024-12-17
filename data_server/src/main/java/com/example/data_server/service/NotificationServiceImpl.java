package com.example.data_server.service;

import com.example.data_server.converters.NotificationEntityGrpcConverter;
import com.example.data_server.repository.NotificationRepository;
import com.example.sep3.grpc.*;
import com.example.shared.entities.domainEntities.Notification;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;

@GrpcService public class NotificationServiceImpl
    extends NotificationServiceGrpc.NotificationServiceImplBase
{
  private NotificationRepository notificationRepository;

  @Autowired public NotificationServiceImpl(
      NotificationRepository notificationRepository)
  {
    this.notificationRepository = notificationRepository;
    System.out.println("NotificationServiceImpl created");
  }

  @Override public void createNotification(NotificationRequestResponse request,
      StreamObserver<EmptyMessage> responseObserver)
  {
    System.out.println("Request for save notification");

    Notification notification = NotificationEntityGrpcConverter.generateNotificationFromNotificationRequestResponse(
        request);

    notificationRepository.save(notification);

    responseObserver.onNext(EmptyMessage.newBuilder().build());
    responseObserver.onCompleted();
  }

  @Override
  public void getNotifications(NotificationsRequest request,
                               StreamObserver<NotificationListResponse> responseObserver)
  {
    System.out.println("Request for getNotifications");

    NotificationListResponse.Builder responseBuilder = NotificationListResponse.newBuilder();
    List<Notification> notifications = notificationRepository.findByUserIdAndUserRole(
            request.getUserId(), request.getUserRole());

    // Sort notifications by a specific field, e.g., `createdDate`
    notifications.sort(Comparator.comparing(Notification::getTimestamp).reversed());
    // Use .reversed() if you want descending order

    for (Notification notification : notifications)
    {
      responseBuilder.addNotifications(
              NotificationEntityGrpcConverter.generateNotificationRequestResponseFromNotification(
                      notification));
    }
    responseObserver.onNext(responseBuilder.build());
    responseObserver.onCompleted();
  }

}

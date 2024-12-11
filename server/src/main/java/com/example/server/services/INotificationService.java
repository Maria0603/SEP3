package com.example.server.services;

import com.example.server.dto.notifications.NotificationResponseDto;

import java.util.List;

public interface INotificationService
{
  List<NotificationResponseDto> getNotifications(String userId, String userRole);
  void sendNotification(NotificationResponseDto notification);
}

package com.example.server.converters;

import com.example.sep3.grpc.NotificationListResponse;
import com.example.sep3.grpc.NotificationRequestResponse;
import com.example.server.dto.notification.NotificationResponseDto;
import com.example.shared.converters.DateTimeConverter;

import java.util.List;
import java.util.stream.Collectors;

public class NotificationDtoGrpcConverter
{
  public static List<NotificationResponseDto> NotificationListResponse_To_ListNotificationResponseDto(
      NotificationListResponse notifications)
  {
    return notifications.getNotificationsList().stream().map(
            notification -> NotificationResponseDto.builder()
                .userId(notification.getUserId())
                .userRole(notification.getUserRole()).type(notification.getType())
                .content(notification.getContent()).timestamp(
                    DateTimeConverter.convertProtoTimestamp_To_LocalDateTime(
                        notification.getTimestamp()))
                .subjectId(notification.getSubjectId()).build())
        .collect(Collectors.toList());
  }
  public static NotificationResponseDto NotificationRequestResponse_To_NotificationResponseDto(NotificationRequestResponse grpc)
  {
    NotificationResponseDto dto = new NotificationResponseDto();
    dto.setUserId(grpc.getUserId());
    dto.setUserRole(grpc.getUserRole());
    dto.setType(grpc.getType());
    dto.setContent(grpc.getContent());
    dto.setTimestamp(DateTimeConverter.convertProtoTimestamp_To_LocalDateTime(grpc.getTimestamp()));
    dto.setSubjectId(grpc.getSubjectId());
    return dto;
  }
}

package com.example.server.dto.notifications;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor public class NotificationResponseDto
{
  private String userId;
  private String userRole;
  private String content;
  private LocalDateTime timestamp;
  private String subjectId;
  private String type;
}

package com.example.server.services.auxServices;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface IEventService
{
  SseEmitter connectUser(String user);
  void sendNotification(String user, String message);
}

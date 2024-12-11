package com.example.server.services.auxServices.Implementations;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service public class EventService
{
  private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

  public EventService()
  {
    System.out.println("EventService created");
  }

  //user = userRole + userId
  public SseEmitter connectUser(String user)
  {
    SseEmitter emitter = new SseEmitter();
    emitters.put(user, emitter);

    emitter.onCompletion(() -> emitters.remove(user));
    emitter.onTimeout(() -> emitters.remove(user));

    sendNotification(user, "You are subscribed");

    return emitter;
  }

  public void sendNotification(String user, String message)
  {
    SseEmitter emitter = emitters.get(user);
    System.out.println("Send notiffffffffffffffffffffffffffffffffff: " + message);
    if (emitter != null)
    {
      try
      {
        emitter.send(SseEmitter.event().data(message));
      }
      catch (IOException e)
      {
        emitters.remove(user); // Remove emitter if it fails
      }
    }
  }

}


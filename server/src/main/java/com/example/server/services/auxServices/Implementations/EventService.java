package com.example.server.services.auxServices.Implementations;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service public class EventService implements com.example.server.services.auxServices.IEventService
{
  private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

  public EventService()
  {
    System.out.println("EventService created");
  }

  //user = userRole + userId
  @Override public SseEmitter connectUser(String user)
  {
    SseEmitter emitter = new SseEmitter(6000000 * 1000L);
    emitters.put(user, emitter);
    sendPingPeriodically(user, emitter);

    emitter.onCompletion(() -> emitters.remove(user));
    emitter.onTimeout(() -> emitters.remove(user));

    return emitter;
  }

  private void sendPingPeriodically(String user, SseEmitter emitter)
  {
    new Thread(() -> {
      try
      {
        while (true)
        {
          if (emitters.containsKey(user))
          {
            emitter.send(
                SseEmitter.event().data("ping"));  // Send a ping message
          }
          Thread.sleep(6000000);
        }
      }
      catch (InterruptedException | IOException e)
      {
        emitters.remove(user);
      }
    }).start();
  }

  @Override public void sendNotification(String user, String message)
  {
    SseEmitter emitter = emitters.get(user);
    if (emitter == null)
    {
      System.out.println("Emitter not found for user: " + user);
      return;
    }
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


package com.example.server.services.auxServices;

public interface IEmailService
{
  void sendNotificationEmail(String to, String subject, String text);
  void sendTestEmail();
}

package com.example.server.services.auxServices.Implementations;

import com.sun.xml.messaging.saaj.packaging.mime.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ws.mime.MimeMessage;

@Service public class EmailService
    implements com.example.server.services.auxServices.IEmailService
{
  private JavaMailSender mailSender;

  @Autowired public EmailService(JavaMailSender mailSender)
  {
    this.mailSender = mailSender;
  }

  @Override public void sendNotificationEmail(String to, String subject,
      String text)
  {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setSubject(subject);
    message.setText(text);
    mailSender.send(message);
  }

  public void sendTestEmail(String email)
  {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(email);
    message.setSubject("Wake up!");
    message.setText("You didn't finish your tasks! EatWise is waiting, but we know where you live...");
    mailSender.send(message);
    System.out.println("Email sent successfully!");
  }

}


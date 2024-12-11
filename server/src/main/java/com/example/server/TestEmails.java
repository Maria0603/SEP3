package com.example.server;

import com.example.server.services.auxServices.IEmailService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component public class TestEmails implements CommandLineRunner
{

  private final IEmailService emailService;

  public TestEmails(IEmailService emailService)
  {
    this.emailService = emailService;
  }

  @Override public void run(String... args) throws Exception
  {
//    emailService.sendTestEmail("teksas77@gmail.com");
//    emailService.sendTestEmail("aleksandra.ignatova2007@gmail.com");
//    emailService.sendTestEmail("masha.moskovko@gmail.com");
//    emailService.sendTestEmail("matej.palas@954gmail.com");
  }
}


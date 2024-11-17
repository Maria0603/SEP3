package com.example.server.config;

import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

//I think this is Jan's part, but I want to play
@Configuration
public class StripeConfig {

  @Value("${stripe.secret.key}")
  private String secretKey;

  @PostConstruct
  public void init() {
    Stripe.apiKey = secretKey;
  }
}


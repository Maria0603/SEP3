package com.example.server.config;

import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class StripeConfig {

  @Value("${stripe.secret.key}")
  private String secretKey;

  @Value("${stripe.public.key}")
  private String publicKey;

  @PostConstruct
  public void init() {
    Stripe.apiKey = secretKey;
  }
}


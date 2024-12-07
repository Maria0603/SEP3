package com.example.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration public class OpenCageConfig
{
  @Value("${opencage.api.key}") private String apiKey;

  public String getApiKey()
  {
    return apiKey;
  }
}


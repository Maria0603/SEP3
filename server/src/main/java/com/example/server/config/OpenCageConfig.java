package com.example.server.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter @Configuration public class OpenCageConfig
{
  @Value("${opencage.api.key}") private String apiKey;
}


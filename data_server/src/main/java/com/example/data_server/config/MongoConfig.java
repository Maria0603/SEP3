package com.example.data_server.config;

import com.example.shared.converters.StringToTimestampConverter;
import com.example.shared.converters.TimestampToStringConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.Arrays;

@Configuration
public class MongoConfig
{
    @Bean
    public MongoCustomConversions customConversions() {
      return new MongoCustomConversions(Arrays.asList(
          new StringToTimestampConverter(),
          new TimestampToStringConverter()
      ));
    }
}

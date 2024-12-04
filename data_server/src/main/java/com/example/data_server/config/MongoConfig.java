package com.example.data_server.config;

import com.example.data_server.utility.StringToTimestampConverter;
import com.example.data_server.utility.TimestampToStringConverter;
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

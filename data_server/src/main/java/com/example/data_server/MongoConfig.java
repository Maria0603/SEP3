package com.example.data_server;

import com.example.data_server.utility.StringToDateDaoConverter;
import com.example.data_server.utility.StringToTimeDaoConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.util.Arrays;

@Configuration
//@EnableMongoRepositories(basePackages = "com.example.data_server.repository")
public class MongoConfig extends AbstractMongoClientConfiguration
{
  @Override
  protected String getDatabaseName() {
    return "sep3Test";  // Replace with your actual database name
  }

  @Override
  public MongoCustomConversions customConversions() {
    return new MongoCustomConversions(Arrays.asList(
        new StringToDateDaoConverter(),
        new StringToTimeDaoConverter()
    ));
  }
/*
  @Bean public MongoCustomConversions customConversions()
  {
    return new MongoCustomConversions(
        Arrays.asList(new StringToDateDaoConverter(),
            new StringToTimeDaoConverter()));
  }

  @Bean public MongoTemplate mongoTemplate(
      MongoDatabaseFactory mongoDatabaseFactory,
      MongoMappingContext mongoMappingContext)
  {
    MappingMongoConverter converter = new MappingMongoConverter(
        mongoDatabaseFactory, mongoMappingContext);
    converter.setCustomConversions(
        customConversions()); // Set the custom conversions here
    return new MongoTemplate(mongoDatabaseFactory, converter);
  }*/
}

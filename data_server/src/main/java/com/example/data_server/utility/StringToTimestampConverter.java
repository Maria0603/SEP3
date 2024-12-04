package com.example.data_server.utility;

import org.springframework.core.convert.converter.Converter;

import java.time.*;

import com.google.protobuf.Timestamp;

import java.time.format.DateTimeFormatter;

public class StringToTimestampConverter implements Converter<String, Timestamp>
{
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

  @Override public Timestamp convert(String source)
  {
    LocalDateTime dateTime = LocalDateTime.parse(source, FORMATTER);
    ZonedDateTime zonedDateTime = dateTime.atZone(ZoneId.systemDefault());

    Instant instant = zonedDateTime.toInstant();
    return Timestamp.newBuilder().setSeconds(instant.getEpochSecond())
        .setNanos(instant.getNano()).build();
  }
}



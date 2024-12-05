package com.example.shared.converters;

import com.google.protobuf.Timestamp;
import org.springframework.core.convert.converter.Converter;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class TimestampToStringConverter implements Converter<Timestamp, String>
  {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    @Override public String convert(Timestamp source)
    {
      Instant instant = Instant.ofEpochSecond(source.getSeconds(),
          source.getNanos());
      ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());

      return zonedDateTime.format(FORMATTER);
    }
  }


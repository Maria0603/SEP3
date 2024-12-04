package com.example.shared.converters;

import com.google.protobuf.Timestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.time.ZonedDateTime;

public class DateTimeConverter
{

  private static final ZoneId DEFAULT_ZONE = ZoneId.systemDefault();

  // Convert java.time.LocalDateTime to google.protobuf.Timestamp
  public static Timestamp convertLocalDateTime_To_ProtoTimestamp(
      LocalDateTime localDateTime)
  {
    // Convert LocalDateTime to ZonedDateTime
    ZonedDateTime zonedDateTime = localDateTime.atZone(DEFAULT_ZONE);
    Instant instant = zonedDateTime.toInstant(); // Convert to Instant
    return Timestamp.newBuilder().setSeconds(instant.getEpochSecond())
        .setNanos(instant.getNano()).build();
  }

  // Convert google.protobuf.Timestamp to java.time.LocalDateTime
  public static LocalDateTime convertProtoTimestamp_To_LocalDateTime(
      Timestamp timestamp)
  {
    Instant instant = Instant.ofEpochSecond(timestamp.getSeconds(),
        timestamp.getNanos());
    ZonedDateTime zonedDateTime = instant.atZone(
        DEFAULT_ZONE); // Convert Instant to ZonedDateTime
    return zonedDateTime.toLocalDateTime(); // Convert ZonedDateTime to LocalDateTime
  }

  // Convert java.util.Date (used in MongoDB) to java.time.LocalDateTime
  public static LocalDateTime convertMongoDbDate_To_LocalDateTime(Date date)
  {
    return LocalDateTime.ofInstant(date.toInstant(),
        DEFAULT_ZONE); // Convert Date to LocalDateTime
  }

  // Convert java.time.LocalDateTime to java.util.Date
  public static Date convertLocalDateTime_To_MongoDbDate(
      LocalDateTime localDateTime)
  {
    Instant instant = localDateTime.atZone(DEFAULT_ZONE)
        .toInstant(); // Convert LocalDateTime to Instant
    return Date.from(instant); // Convert Instant to Date
  }

  // Convert java.util.Date to google.protobuf.Timestamp
  public static Timestamp convertMongoDbDate_To_ProtoTimestamp(Date date)
  {
    Instant instant = date.toInstant(); // Convert Date to Instant
    return Timestamp.newBuilder().setSeconds(instant.getEpochSecond())
        .setNanos(instant.getNano()).build();
  }

  // Convert google.protobuf.Timestamp to java.util.Date
  public static Date convertProtoTimestamp_To_MongoDbDate(Timestamp timestamp)
  {
    Instant instant = Instant.ofEpochSecond(timestamp.getSeconds(),
        timestamp.getNanos());
    return Date.from(instant); // Convert Instant to Date
  }
}

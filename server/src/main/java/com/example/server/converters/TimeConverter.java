package com.example.server.converters;

import com.example.sep3.grpc.Time;
import com.example.server.dto.date_time.TimeDto;
import com.example.shared.dao.TimeDao;

import java.time.LocalTime;

public class TimeConverter
{
  // Convert TimeDto to TimeDao
  public static TimeDao toTimeDao(TimeDto timeDto)
  {
    return new TimeDao(timeDto.getHour(), timeDto.getMinute());
  }

  // Convert TimeDao to TimeDto
  public static TimeDto convertTimeDaoToTimeDto(TimeDao timeDao)
  {
    TimeDto timeDto = new TimeDto();
    timeDto.setHour(timeDao.getHour());
    timeDto.setMinute(timeDao.getMinute());
    return timeDto;
  }

  // Convert TimeDao to Time gRPC message
  public static Time convertTimeDaoToGrpcTime(TimeDao timeDao)
  {
    if (timeDao == null)
    {
      return null;
    }
    return Time.newBuilder().setHour(timeDao.getHour())
        .setMinute(timeDao.getMinute()).build();
  }

  // Convert TimeDto to Time gRPC message
  public static Time convertTimeDtoToGrpcTime(TimeDto timeDto)
  {
    if (timeDto == null)
    {
      return null;
    }
    return Time.newBuilder().setHour(timeDto.getHour())
        .setMinute(timeDto.getMinute()).build();
  }

  public static TimeDto convertGrpcTimeToTimeDto(Time time)
  {
    TimeDto timeDto = new TimeDto();
    timeDto.setHour(time.getHour());
    timeDto.setMinute(time.getMinute());
    return timeDto;
  }

  //Convert TimeDto to LocalTime
  public static LocalTime convertTimeDtoToLocalTime(TimeDto timeDto)
  {
    return LocalTime.of(timeDto.getHour(), timeDto.getMinute());
  }

}

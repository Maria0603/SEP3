﻿package com.example.server.utility;

import com.example.sep3.grpc.Time;
import com.example.server.dto.TimeDto;
import com.example.shared.dao.TimeDao;

public class TimeConverter
{
  // Convert TimeDto to TimeDao
  public static TimeDao toTimeDao(TimeDto timeDto)
  {
    return new TimeDao(timeDto.getHour(), timeDto.getMinute());
  }

  // Convert TimeDao to TimeDto
  public static TimeDto toTimeDto(TimeDao timeDao)
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
}
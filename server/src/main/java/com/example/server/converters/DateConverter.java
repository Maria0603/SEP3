package com.example.server.converters;

import com.example.sep3.grpc.Date;
import com.example.server.dto.DateDto;
import com.example.shared.dao.DateDao;

public class DateConverter
{
  // Convert DateDto to DateDao
  public static DateDao toDateDao(DateDto dateDto)
  {
    return new DateDao(dateDto.getYear(), dateDto.getMonth(), dateDto.getDay());
  }

  // Convert DateDao to DateDto
  public static DateDto toDateDto(DateDao dateDao)
  {
    DateDto dateDto = new DateDto();
    dateDto.setYear(dateDao.getYear());
    dateDto.setMonth(dateDao.getMonth());
    dateDto.setDay(dateDao.getDay());
    return dateDto;
  }

  // Convert DateDao to Date gRPC message
  public static Date convertDateDaoToGrpcDate(DateDao dateDao)
  {
    if (dateDao == null)
      return null;
    return Date.newBuilder().setYear(dateDao.getYear())
        .setMonth(dateDao.getMonth()).setDay(dateDao.getDay()).build();
  }

  // Convert DateDto to Date gRPC message
  public static Date convertDateDtoToGrpcDate(DateDto dateDto)
  {
    if (dateDto == null)
      return null;
    return Date.newBuilder().setYear(dateDto.getYear())
        .setMonth(dateDto.getMonth()).setDay(dateDto.getDay()).build();
  }

  public static DateDto convertGrpcDateToDateDto(Date date)
  {
    if (date == null)
      return null;
    DateDto dateDto = new DateDto();
    dateDto.setYear(date.getYear());
    dateDto.setMonth(date.getMonth());
    dateDto.setDay(date.getDay());
    return dateDto;
  }
}

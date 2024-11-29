package com.example.data_server.utility;

import com.example.sep3.grpc.Date;
import com.example.sep3.grpc.Time;
import com.example.shared.dao.auxDao.DateDao;
import com.example.shared.dao.auxDao.TimeDao;

public class DateTimeConverter {
  // Convert DateDao to Date gRPC message
  public static Date convertDateDaoToGrpcDate(DateDao dateDao) {
    if (dateDao == null)
      return null;
    return Date.newBuilder().setYear(dateDao.getYear())
        .setMonth(dateDao.getMonth()).setDay(dateDao.getDay()).build();
  }

  // Convert gRPC Date to DateDao
  public static DateDao convertGrpcDateToDateDao(Date grpcDate) {
    if (grpcDate == null)
      return null;
    return new DateDao(grpcDate.getYear(), grpcDate.getMonth(),
        grpcDate.getDay());
  }

  // Convert TimeDao to Time gRPC message
  public static Time convertTimeDaoToGrpcTime(TimeDao timeDao) {
    if (timeDao == null)
      return null;
    return Time.newBuilder().setHour(timeDao.getHour())
        .setMinute(timeDao.getMinute()).build();
  }

  // Convert gRPC Time to TimeDao
  public static TimeDao convertGrpcTimeToTimeDao(Time grpcTime) {
    if (grpcTime == null)
      return null;
    return new TimeDao(grpcTime.getHour(), grpcTime.getMinute());
  }

  // Convert com.google.type.Date to DateDao
  public static DateDao convertGoogleDateToDateDao(
      com.google.type.Date googleDate) {
    if (googleDate == null) {
      return null;
    }
    return new DateDao(googleDate.getYear(), googleDate.getMonth(),
        googleDate.getDay());
  }

  // Convert DateDao to com.google.type.Date
  public static com.google.type.Date convertDateDaoToGoogleDate(
      DateDao dateDao) {
    if (dateDao == null) {
      return null;
    }
    return com.google.type.Date.newBuilder().setYear(dateDao.getYear())
        .setMonth(dateDao.getMonth()).setDay(dateDao.getDay()).build();
  }

  //Get the current date in DateDao format
  public static DateDao getCurrentDateDao() {
    java.util.Date date = new java.util.Date();
    return new DateDao(date.getYear() + 1900, date.getMonth() + 1, date.getDate());
  }

  //Get current time in TimeDao format
  public static TimeDao getCurrentTimeDao() {
    java.util.Date date = new java.util.Date();
    return new TimeDao(date.getHours(), date.getMinutes());
  }

}

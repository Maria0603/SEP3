package com.example.shared.dao.auxDao;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "time") public class TimeDao
{
  @Field("hour") private int hour;
  @Field("minute") private int minute;

  public TimeDao(int hour, int minute)
  {
    this.hour = hour;
    this.minute = minute;
  }

  public int getHour()
  {
    return hour;
  }

  public void setHour(int hour)
  {
    this.hour = hour;
  }

  public int getMinute()
  {
    return minute;
  }

  public void setMinute(int minute)
  {
    this.minute = minute;
  }

}

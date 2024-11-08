package com.example.shared.dao;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "time")
public class TimeDao
{
  private int hour, minute;

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

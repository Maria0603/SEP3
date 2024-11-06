package com.example.shared.dao;

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

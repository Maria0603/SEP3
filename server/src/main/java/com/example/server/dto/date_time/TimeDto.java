package com.example.server.dto.date_time;

public class TimeDto
{
  private int hour;
  private int minute;

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

  @Override public boolean equals(Object obj)
  {
    if(!(obj instanceof TimeDto other))
      return false;

    return this.hour == other.hour && this.minute == other.minute;
  }
}


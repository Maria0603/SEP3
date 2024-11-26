package com.example.server.dto.date_time;

public class DateDto {
  private int year;
  private int month;
  private int day;

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public int getMonth() {
    return month;
  }

  public void setMonth(int month) {
    this.month = month;
  }

  public int getDay() {
    return day;
  }

  public void setDay(int day) {
    this.day = day;
  }

  @Override public boolean equals(Object obj)
  {
    if(!(obj instanceof DateDto other))
      return false;

    return this.year == other.year && this.month == other.month && this.day == other.day;
  }
}


package com.example.shared.dao.auxDao;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "date") public class DateDao
{
  @Field("year") private int year;
  @Field("month") private int month;
  @Field("day") private int day;

  public DateDao(int year, int month, int day)
  {
    this.year = year;
    this.month = month;
    this.day = day;
  }

  public int getMonth()
  {
    return month;
  }

  public void setMonth(int month)
  {
    this.month = month;
  }

  public int getDay()
  {
    return day;
  }

  public void setDay(int day)
  {
    this.day = day;
  }

  public int getYear()
  {
    return year;
  }

  public void setYear(int year)
  {
    this.year = year;
  }

}

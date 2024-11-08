package com.example.shared.dao;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "date")
public class DateDao
{
  private int year, month, day;


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

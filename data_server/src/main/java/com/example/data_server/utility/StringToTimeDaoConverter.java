package com.example.data_server.utility;

import com.example.shared.dao.TimeDao;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;

public class StringToTimeDaoConverter implements Converter<String, TimeDao>
{

  @Override public TimeDao convert(String source)
  {
    if (source == null || source.isEmpty())
      return null;  // or throw an exception if it's required

    // Assuming the format is "HH:mm"
    LocalTime time = LocalTime.parse(source);

    return new TimeDao(time.getHour(), time.getMinute());
  }
}


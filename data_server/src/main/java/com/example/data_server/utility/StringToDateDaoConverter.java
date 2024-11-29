package com.example.data_server.utility;

import com.example.shared.dao.auxDao.DateDao;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;

public class StringToDateDaoConverter implements Converter<String, DateDao>
{

  @Override public DateDao convert(String source)
  {
    if (source == null || source.isEmpty())
      return null;  // or throw an exception if it's required

    // Assuming the format is "YYYY-MM-DD"
    LocalDate date = LocalDate.parse(source);

    return new DateDao(date.getYear(), date.getMonthValue(),
        date.getDayOfMonth());
  }
}


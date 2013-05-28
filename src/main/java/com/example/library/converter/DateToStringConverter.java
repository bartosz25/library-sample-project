package com.example.library.converter;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

// TODO : voir car probablement aussi à virer
public class DateToStringConverter implements Converter<Date, String> {
    final Logger logger = LoggerFactory.getLogger(DateToStringConverter.class);
    private String format = "yyyy-MM-dd";

    public String convert(Date date) {
        logger.info("=============> converting from Date " + date);
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setLenient(false);
        return formatter.format(date);
    }
}
package library.formatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.Formatter;

// TODO : voir car à 99% pas utilisé
public class DateFormatter implements Formatter<Date>
{
    final Logger logger = LoggerFactory.getLogger(DateFormatter.class);
    public String print(Date date, Locale locale)
    {
        logger.info("======> formatting date " + date);
        return "2012-02-03";
    }
    public Date parse(String text, Locale locale) throws ParseException
    {
        logger.info("======> formatting to date " + text);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", locale);
        dateFormat.setLenient(false);
        return dateFormat.parse(text);
    }
    public String toString()
    {
        return "2012-03-03";
    }
}
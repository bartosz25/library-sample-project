package library.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

// TODO : voir si utilisé, car probablement à virer à cause du PropertyEditor DateCustomEditor
public class StringToDateConverter implements Converter<String, Date> {
    final Logger logger = LoggerFactory.getLogger(StringToDateConverter.class);
    private String format = "yyyy-MM-dd";

    public Date convert(String date) {
        logger.info("=============> converting to Date " + date);
        Date d = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat(format);
            df.setLenient(false);
            d = df.parse(date);
        } catch(ParseException e) {
            logger.error("An error occured on parsing date " + date, e);
        }
        return d;
    }
}
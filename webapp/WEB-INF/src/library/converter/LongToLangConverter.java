package library.converter;

import library.model.entity.Lang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

public class LongToLangConverter implements Converter<Long, Lang> {
    final Logger logger = LoggerFactory.getLogger(LongToLangConverter.class);

    public Lang convert(Long lang) {
        logger.info("============> LongToLang = " + lang);
        return new Lang();
    }
}
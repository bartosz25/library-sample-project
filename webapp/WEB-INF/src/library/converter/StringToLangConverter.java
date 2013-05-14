package library.converter;

import library.model.entity.Lang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

public class StringToLangConverter implements Converter<String, Lang> {
    final Logger logger = LoggerFactory.getLogger(StringToLangConverter.class);

    public Lang convert(String lang) {
        logger.info("============> StringToLang = " + lang);
        return new Lang();
    }
}
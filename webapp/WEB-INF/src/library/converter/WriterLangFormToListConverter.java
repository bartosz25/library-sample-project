package library.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import library.form.WriterLangForm;
import library.model.entity.Lang;
import library.model.entity.WriterLang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

public class WriterLangFormToListConverter implements Converter<WriterLangForm, List<WriterLang>> {
    final Logger logger = LoggerFactory.getLogger(WriterLangFormToListConverter.class);
    
    /**
     * Converts POST received WriterLangForm elements into WriterLang objects, ready to be
     * inserted into the database.
     * It's the reason why we add only the none null entries to final List<WriterLang>.
     */
    public List<WriterLang> convert(WriterLangForm writerLangForm) {
        List<WriterLang> result = new ArrayList<WriterLang>();
        logger.info("============> convert to Writer " + writerLangForm.getWriter());
        logger.info("============> WriterLangFormToListConverter = " + writerLangForm);
        for (Long key: writerLangForm.getWriterLang().keySet()) {
            Lang lang = new Lang();
            lang.setId(key);
            Map<String, Object> entry = writerLangForm.getWriterLang().get(key);
            logger.info("=========> doing operation ["+key+"] for entry = " + entry);
            for (String entryKey : entry.keySet()) {
                logger.info("=========> operation ["+entryKey+"] " + entry.get(entryKey));
                if (entry.get(entryKey) != null && !((String) entry.get(entryKey)).equals("")) {
                    WriterLang writerLang = new WriterLang();
                    writerLang.setWriterLangPK(lang, writerLangForm.getWriter(), entryKey);
                    // writerLang.setWriter(writerLangForm.getWriter());
                    // writerLang.setLang(lang);
                    // writerLang.setType(entryKey);
                    writerLang.setValue((String) entry.get(entryKey));
                    logger.info("=============> adding " + writerLang);
                    result.add(writerLang);
                }
            }
        }
        logger.info("============> WriterLangFormToListConverter after conversion = " + result);
        return result;
    }
}
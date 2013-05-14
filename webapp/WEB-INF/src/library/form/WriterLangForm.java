package library.form;

import java.util.Map;

import library.model.entity.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WriterLangForm  {
    final Logger logger = LoggerFactory.getLogger(WriterLangForm.class);
    private Map<Long, Map<String, Object>> writerLang;
    private Writer writer;
    
    public Map<Long, Map<String, Object>> getWriterLang() {
        return writerLang;
    }

    public void setWriterLang(Map<Long, Map<String, Object>> writerLang) {
        logger.info("===========> Sett " + writerLang);
        this.writerLang = writerLang;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    public Writer getWriter() {
        return writer;
    }

    public String toString() {
        logger.info("===========> To String " + writerLang);
        StringBuilder builder = new StringBuilder();
        for (Long key : writerLang.keySet()) {
            builder.append(writerLang.get(key));
        }
        return builder.toString();
    }
}
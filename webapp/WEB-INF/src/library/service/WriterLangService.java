package library.service;

import java.util.List;
import java.util.Map;

import library.model.entity.Lang;
import library.model.entity.Writer;
import library.model.entity.WriterLang;

public interface WriterLangService {
    // public List<Writer> findById();
    // public Writer getById(long id);
    public WriterLang addNew(WriterLang writerLang) throws Exception;
    public Map<String, WriterLang> getForWriter(Writer writer);
    public List<WriterLang> getAllWritersByLang(Lang lang);
    // public void delete(Writer writer);
}
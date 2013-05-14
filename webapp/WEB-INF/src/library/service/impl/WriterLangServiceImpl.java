package library.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import library.model.entity.Lang;
import library.model.entity.Writer;
import library.model.entity.WriterLang;
import library.model.entity.WriterLangPK;
import library.model.repository.WriterLangRepository;
import library.service.WriterLangService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service("writerLangService")
@Repository
// @Transactional
public class WriterLangServiceImpl implements WriterLangService {
    final Logger logger = LoggerFactory.getLogger(WriterLangServiceImpl.class);
    @Autowired
    private WriterLangRepository writerLangRepository;

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'WRITERLANG_ADD')")
    public WriterLang addNew(WriterLang writerLang) throws Exception {
        // logger.info("Saving writerLang " + writerLang);
        return writerLangRepository.save(writerLang);
    }
    
    @Override
    public Map<String, WriterLang> getForWriter(Writer writer) {
        List<WriterLang> entries = writerLangRepository.getForWriter(writer);
        Map<String, WriterLang> result = new HashMap<String, WriterLang>();
        for (WriterLang entry : entries) {
            WriterLangPK writerLangPK = entry.getWriterLangPK();
            logger.info("============> " + entry);
            logger.info("============> " + writerLangPK);
            result.put(WriterLang.getMapEntryKey(entry.getLang(), writerLangPK.getType()), entry);
        }
        return result;
    }
    
    @Override
    public List<WriterLang> getAllWritersByLang(Lang lang) {
        return writerLangRepository.getAllWritersByLang(lang);
    }
}
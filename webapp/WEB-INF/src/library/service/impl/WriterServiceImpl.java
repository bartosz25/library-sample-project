package library.service.impl;

import java.util.List;

import library.model.entity.Writer;
import library.model.repository.WriterRepository;
import library.service.WriterService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service("writerService")
@Repository
// @Transactional
public class WriterServiceImpl implements WriterService {
    final Logger logger = LoggerFactory.getLogger(WriterServiceImpl.class);
    @Autowired
    private WriterRepository writerRepository;

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'WRITER_ADD')")
    public Writer addNew(Writer writer) throws Exception {
        logger.info("Saving writer " + writer);
        return writerRepository.save(writer);
    }

    @Override
    public Writer getById(long id) {
        return writerRepository.findOne(id);
    }
    
    @Override
    public List<Writer> getAll() {
        return writerRepository.getAll();
    }
}
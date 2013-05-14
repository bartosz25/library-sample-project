package library.service;

import java.util.List;

import library.model.entity.Writer;

public interface WriterService {
    // public List<Writer> findById();
    public Writer getById(long id);
    public Writer addNew(Writer writer) throws Exception;
    public List<Writer> getAll();
    // public void delete(Writer writer);
}
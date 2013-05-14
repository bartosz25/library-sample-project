package library.service;

import java.util.List;

import library.model.entity.Logger;

public interface LoggerService {
    public List<Logger> findById();
    public Logger save(Logger logger);
    public void delete(Logger logger);
}
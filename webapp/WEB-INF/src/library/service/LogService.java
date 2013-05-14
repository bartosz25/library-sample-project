package library.service;

import java.util.List;

import library.model.entity.Log;

public interface LogService {
    public List<Log> findById();
    public Log save(Log logger);
    public void delete(Log logger);
}
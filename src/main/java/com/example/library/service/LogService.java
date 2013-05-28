package com.example.library.service;

import java.util.List;

import com.example.library.model.entity.Log;

public interface LogService {
    public List<Log> findById();
    public Log save(Log logger);
    public void delete(Log logger);
}
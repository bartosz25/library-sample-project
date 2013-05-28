package com.example.library.service;

import java.util.List;

import com.example.library.model.entity.Logger;

public interface LoggerService {
    public List<Logger> findById();
    public Logger save(Logger logger);
    public void delete(Logger logger);
}
package com.example.library.service;

import java.util.List;

import com.example.library.model.entity.Suggestion;

public interface SuggestionService {
    public Suggestion addNew(Suggestion suggestion) throws Exception;
    public List<Suggestion> findByTitleContaining(String title);
    public Suggestion findByTitle(String title);
    // public List<Suggestion> findById();
    // public Suggestion save(Suggestion suggestion);
    // public void delete(Suggestion suggestion);
}
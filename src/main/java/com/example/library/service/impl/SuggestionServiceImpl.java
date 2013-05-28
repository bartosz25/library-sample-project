package com.example.library.service.impl;

import java.util.List;

import com.example.library.model.entity.Suggestion;
import com.example.library.model.repository.SuggestionRepository;
import com.example.library.service.SuggestionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Service("suggestionService")
public class SuggestionServiceImpl implements SuggestionService {
    final Logger logger = LoggerFactory.getLogger(SuggestionServiceImpl.class);
    @Autowired
    private SuggestionRepository suggestionRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;

    @PreAuthorize("hasRole('ROLE_USER')")
    @Override
    public Suggestion addNew(Suggestion suggestion)  throws Exception {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            Suggestion suggestionByTitle = findByTitle(suggestion.getTitle());
            if (suggestionByTitle != null) {
                suggestionByTitle.setQuantity(suggestionByTitle.getQuantity() + 1);
                suggestion = suggestionByTitle;
            } else {
                suggestion.setQuantity(suggestion.getQuantity() + 1);
                suggestion.setState(Suggestion.STATE_NEW);
                suggestion = suggestionRepository.save(suggestion);
            }
            transactionManager.commit(status);
        } catch (Exception e) {
            logger.error("An exception occured on saving Suggestion", e);
            suggestion = null;
            transactionManager.rollback(status);
            throw new Exception("An exception occured on saving Suggestion", e);
        }
        return suggestion;
    }
    
    @Override
    public List<Suggestion> findByTitleContaining(String title) {
        return suggestionRepository.findByTitleContaining(title);
    }
    
    @Override
    public Suggestion findByTitle(String title) {
        return suggestionRepository.findByTitle(title);
    }
}
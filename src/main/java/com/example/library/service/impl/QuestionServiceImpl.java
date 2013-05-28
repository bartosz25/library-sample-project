package com.example.library.service.impl;

import java.util.Date;

import com.example.library.model.entity.Question;
import com.example.library.model.repository.QuestionRepository;
import com.example.library.service.QuestionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * TODO : 
 * - attribuer la question à un administrateur possédant dans les droits REPLY_WRITE ou le rôle RESPONDER
 */ 
@Service("questionService")
public class QuestionServiceImpl implements QuestionService {
    final Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    public Question addNew(Question question) throws Exception {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            question.setDate(new Date());
            question.setState(Question.STATE_NEW);
            question = questionRepository.save(question);
            
            // TODO : attribuer cette question à quelqu'un
            
            transactionManager.commit(status);
        } catch (Exception e) {
            logger.error("An exception occured on saving Question", e);
            question = null;
            transactionManager.rollback(status);
            throw new Exception(e);
        }
        return question;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'QUESTION_READ')")
    public Question markAsRead(Question question) {
        if (!question.wasReplied()) {
            question.setState(Question.STATE_READ);
            question = questionRepository.save(question);
        }
        return question;
    }
}
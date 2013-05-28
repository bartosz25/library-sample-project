package com.example.library.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.example.library.model.entity.Question;
import com.example.library.model.entity.Reply;
import com.example.library.model.repository.QuestionRepository;
import com.example.library.model.repository.ReplyRepository;
import com.example.library.service.ReplyService;
import com.example.library.tools.MailerTool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Service("replyService")
public class ReplyServiceImpl implements ReplyService {
    private static final String TPL_REPLY_NOTIF = "replyNotif";
    final Logger logger = LoggerFactory.getLogger(ReplyServiceImpl.class);
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private MailerTool mailerTool;

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'REPLY_ADD')")
    public Reply addNew(Reply reply) throws Exception {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            reply.setDate(new Date());
            reply.setState(Reply.STATE_NEW);
            reply = replyRepository.save(reply);
            // mark question's state as replied
            Question question = reply.getQuestion();
            question.setState(Question.STATE_REPLIED);
            questionRepository.save(question);
            // send reply notification to subscriber
            HashMap<String, Object> mailData = new HashMap<String, Object>();
            mailData.put("config", TPL_REPLY_NOTIF);
            mailData.put("to", reply.getQuestion().getSubscriber().getEmail());
            Map<String, Object> tplVars = new HashMap<String, Object>();
            tplVars.put("reply", reply);
            mailerTool.setMailData(mailData);
            mailerTool.setVars(tplVars);
            mailerTool.send();
            transactionManager.commit(status);
        } catch(Exception e) {
            logger.error("An exception occured on saving Reply", e);
            reply = null;
            transactionManager.rollback(status);
            throw new Exception(e);
        }
        return reply;
    }
}
package com.example.library.service.impl;

import java.util.Date;
import java.util.List;

import com.example.library.model.entity.Admin;
import com.example.library.model.entity.Chat;
import com.example.library.model.entity.Subscriber;
import com.example.library.model.repository.AdminRepository;
import com.example.library.model.repository.ChatRepository;
import com.example.library.service.ChatService;

import org.apache.commons.beanutils.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * TODOS : 
 * - rajouter pagination dans getByIdSubscriberOrAdmin
 * - exporter "http://localhost:8080/cometd", "/echo"
 * - addNew() : passer Channel en paramètre
 */
@Service("chatService")
public class ChatServiceImpl implements ChatService {
    final Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);
    public final static String TYPE_ADMIN = "admin";
    public final static String TYPE_SUBSCRIBER = "subscriber";
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;
    
    @Override
    public List<Chat> getByIdSubscriberOrAdmin(long id, int state, String type) {
        if (type != null) {
            boolean isSimple = false;
            Object[] paramsSimple = new Object[1];
            Object[] paramsWithState = new Object[2];
            String ownerMethod = "BySubscriber";
            paramsSimple[0] = id;
            paramsWithState[0] = id;
            if (type.equals(TYPE_ADMIN)) {
                ownerMethod = "ByAdmin";
            }
            String stateMethod = "";
            isSimple = true;
            if (state == Chat.STATE_NOT_READ) {
                paramsWithState[1] = Chat.STATE_NOT_READ;
                stateMethod = "New";
            } else if (state == Chat.STATE_READ) {
                paramsWithState[1] = Chat.STATE_READ;
                stateMethod = "Old";
            }
            String repMethodName = "getMessages"+ownerMethod+stateMethod;
            try {
                if (isSimple) return (List<Chat>)MethodUtils.invokeMethod(chatRepository, repMethodName, paramsSimple);
                return (List<Chat>)MethodUtils.invokeMethod(chatRepository, repMethodName, paramsWithState);
            } catch (Exception e) {
                logger.error("An exception occured on calling " + repMethodName, e);
            }
        }
        return null;
    }
    
    @Override
    public Chat addNew(Chat chat, Subscriber subscriber, Admin admin) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            if (admin == null) {
                // TODO : actually we get the first returned admin, normally this admin must be available and he is not 
                List<Admin> admins = adminRepository.getAvailableAdmin();
                admin = admins.get(0);
            }
            if (chat.getSubscriber() == null) chat.setSubscriber(subscriber);
            logger.info("==========> " + admin);
            logger.info("==========> " + new Date().getTime());
            chat.setState(Chat.STATE_NOT_READ);
            chat.setChatPK(new Date().getTime(), subscriber, admin);
            chat.setAdmin(admin);
            chatRepository.save(chat);
            transactionManager.commit(status);
        } catch (Exception e) {
            logger.error("An exception occured on saving chat entry", e);
            chat = null;
            transactionManager.rollback(status);
        }
        return chat;
    }

    @Override
    public Page<Chat> getLastEntryBySubscriber(Subscriber subscriber) {
        Pageable pageable = new PageRequest(1, 1);
        return chatRepository.getLastEntryBySubscriber(subscriber.getId(), pageable);
    }
}
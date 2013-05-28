package com.example.library.service;

import java.util.List;

import com.example.library.model.entity.Admin;
import com.example.library.model.entity.Chat;
import com.example.library.model.entity.Subscriber;

import org.springframework.data.domain.Page;

public interface ChatService {
    public List<Chat> getByIdSubscriberOrAdmin(long id, int state, String type);
    public Chat addNew(Chat chat, Subscriber subscriber, Admin admin);
    public Page<Chat> getLastEntryBySubscriber(Subscriber subscriber);
}
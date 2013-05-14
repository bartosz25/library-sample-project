package library.service;

import java.util.List;

import library.model.entity.Admin;
import library.model.entity.Chat;
import library.model.entity.Subscriber;

import org.springframework.data.domain.Page;

public interface ChatService {
    public List<Chat> getByIdSubscriberOrAdmin(long id, int state, String type);
    public Chat addNew(Chat chat, Subscriber subscriber, Admin admin);
    public Page<Chat> getLastEntryBySubscriber(Subscriber subscriber);
}
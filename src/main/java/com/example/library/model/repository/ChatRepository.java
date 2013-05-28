package com.example.library.model.repository;

import com.example.library.model.entity.Chat;
import com.example.library.model.entity.ChatPK;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ChatRepository  extends CrudRepository<Chat, ChatPK> {
    @Query("SELECT ch FROM Chat ch WHERE ch.chatPK.idSubscriber = :idSubscriber")
    public Page<Chat> getMessagesBySubscriber(@Param("idSubscriber") long idSubscriber, Pageable pageable);

    @Query("SELECT ch FROM Chat ch WHERE ch.chatPK.idAdmin = :idAdmin")
    public Page<Chat> getMessagesByAdmin(@Param("idAdmin") long idAdmin, Pageable pageable);

    @Query("SELECT ch FROM Chat ch WHERE ch.chatPK.idSubscriber = :idSubscriber AND ch.state  = :state")
    public Page<Chat> getMessagesBySubscriberNew(@Param("idSubscriber") long idSubscriber, @Param("state") int state, Pageable pageable);

    @Query("SELECT ch FROM Chat ch WHERE ch.chatPK.idAdmin = :idAdmin AND ch.state = :state")
    public Page<Chat> getMessagesByAdminNew(@Param("idAdmin") long idAdmin, @Param("state") int state, Pageable pageable);

    @Query("SELECT ch FROM Chat ch WHERE ch.chatPK.idSubscriber = :idSubscriber AND ch.state  = :state")
    public Page<Chat> getMessagesBySubscriberOld(@Param("idSubscriber") long idSubscriber, @Param("state") int state, Pageable pageable);

    @Query("SELECT ch FROM Chat ch WHERE ch.chatPK.idAdmin = :idAdmin AND ch.state = :state")
    public Page<Chat> getMessagesByAdminOld(@Param("idAdmin") long idAdmin, @Param("state") int state, Pageable pageable);

   @Query("SELECT ch FROM Chat ch WHERE ch.chatPK.idSubscriber = :idSubscriber ORDER BY ch.chatPK.time")
   public Page<Chat> getLastEntryBySubscriber(@Param("idSubscriber") long idSubscriber, Pageable pageable);
}
package com.example.library.model.repository;

import java.util.List;

import com.example.library.model.entity.Subscriber;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SubscriberRepository  extends CrudRepository<Subscriber, Long> {
    @Transactional(readOnly = true)
    @Query("SELECT s FROM Subscriber s WHERE id = :id")
    public Subscriber findNonConfirmedById(@Param("id") long id);
    @Transactional(readOnly = true)
    @Query("SELECT s FROM Subscriber s WHERE login = :login")
    public Subscriber loadByUsername(@Param("login") String login);
    @Query("SELECT COUNT(s.id) FROM Subscriber s")
    public long countAllUsers();
    @Modifying
    @Query("UPDATE Subscriber s SET s.email = :email WHERE s.id = :id")
    public void updateEmail(@Param("email") String email, @Param("id") long id);
    @Modifying
    @Query("UPDATE Subscriber s SET s.password = :password WHERE s.id = :id")
    public void updatePassword(@Param("password") String password, @Param("id") long id);
    @Query("SELECT s FROM Subscriber s WHERE DATEDIFF(s.created, CURDATE()) = :days AND s.confirmed = :confirmed AND s.revival < :days")
    public List<Subscriber> getNonConfirmedByDays(@Param("days") int days, @Param("confirmed") int confirmed);
}
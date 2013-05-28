package com.example.library.model.repository;

import java.util.List;

import com.example.library.model.entity.Subscriber;
import com.example.library.model.entity.Suggestion;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface SuggestionRepository extends CrudRepository<Suggestion, Long> {
    public List<Suggestion> findByTitleContaining(String title);
    public Suggestion findByTitle(String title);

    @Query("SELECT s FROM Suggestion s WHERE s.subscriber = :subscriber")
    public List<Suggestion> foundBySubscriber(@Param("subscriber") Subscriber subscriber);
}
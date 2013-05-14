package library.model.repository;

import java.util.List;

import library.model.entity.Question;
import library.model.entity.Subscriber;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository  extends CrudRepository<Question, Long> {
    @Query("SELECT s FROM Suggestion s WHERE s.subscriber = :subscriber")
    public List<Question> foundBySubscriber(@Param("subscriber") Subscriber subscriber);
}
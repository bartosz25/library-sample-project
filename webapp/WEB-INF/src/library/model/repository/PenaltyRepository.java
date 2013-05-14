package library.model.repository;

import java.util.List;

import library.model.entity.Penalty;
import library.model.entity.PenaltyPK;
import library.model.entity.Subscriber;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface PenaltyRepository  extends CrudRepository<Penalty, PenaltyPK> {
    @Query("SELECT pe FROM Penalty pe WHERE pe.penaltyPK.idSubscriber = :subscriber AND pe.state != :state")
    public List<Penalty> getPenaltiesBySubscriber(@Param("subscriber") long idSubscriber, @Param("state") int state);
    @Query("SELECT SUM(pe.amount) FROM Penalty pe WHERE pe.penaltyPK.idSubscriber = :subscriber")
    public double getSumBySubscriberId(@Param("subscriber") long idSubscriber);
    @Query("SELECT pe FROM Penalty pe WHERE pe.penaltyPK.time = :time AND pe.penaltyPK.idSubscriber = :subscriber")
    public Penalty getPenaltyByUserAndId(@Param("time") long time, @Param("subscriber") long idSubscriber);
    public List<Penalty> findBySubscriber(Subscriber subscriber);
}
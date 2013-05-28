package com.example.library.model.repository;

import java.util.Date;
import java.util.List;

import com.example.library.model.entity.Subscriber;
import com.example.library.model.entity.Subscription;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {
    @Query("SELECT s FROM Subscription s WHERE s.id = :id AND s.subscriber = :subscriber")
    public Subscription findByIdAndSubscriber(@Param("id") long id, @Param("subscriber") Subscriber subscriber);

    @Query("SELECT s FROM Subscription s WHERE s.subscriber = :subscriber AND s.end > DATE(:today)")
    public List<Subscription> getActifBySubscriber(@Param("subscriber") Subscriber subscriber, @Param("today") Date today);

// Requête à utiliser est SELECT * FROM `subscription` WHERE ((start_sub BETWEEN DATE("2013-01-01") AND DATE("2013-04-01")) OR (end_sub BETWEEN DATE("2013-01-01") AND DATE("2013-04-01"))) AND subscriber_id_su = 12 + rajouter un ORDER BY start_sub

// dans la comparaison on récupère toutes les dates du between et on compare la date[j] avec date[j+1] [si j+1 != null)
// si la comparaison donne une période "trou", on le rajoute dans une liste 
// ensuite on parcourt la liste et vérifie si la période "trou" s'inscrit dans une période l'abonnement offert

    @Query("SELECT s FROM Subscription s WHERE s.subscriber = :subscriber AND s.start <= DATE(:start) AND s.end = DATE(:end)))")
    public List<Subscription> getOverlapSubscriptionBySubscriber(@Param("subscriber") Subscriber subscriber, @Param("start") Date start, @Param("end") Date end);

    @Query("SELECT SUM(s.amount) FROM Subscription s WHERE s.subscriber = :subscriber")
    public double getSumBySubscriber(@Param("subscriber") Subscriber subscriber);

    public List<Subscription> findBySubscriber(Subscriber subscriber);
}
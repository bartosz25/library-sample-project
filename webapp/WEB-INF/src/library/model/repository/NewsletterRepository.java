package library.model.repository;

import java.util.Date;

import library.model.entity.Admin;
import library.model.entity.Newsletter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface NewsletterRepository extends CrudRepository<Newsletter, Long> {
    @Query("SELECT n, a FROM Newsletter n JOIN n.admin a WHERE n.state < :state AND n.sendTime < :date ORDER BY n.sendTime ASC")
    public Page<Object[]> getToSend(@Param("date") Date date, @Param("state") int state, Pageable pageable);

    @Query("SELECT n FROM Newsletter n WHERE n.id = :id AND n.admin = :admin")
    public Newsletter getByIdAndAdmin(@Param("id") long id, @Param("admin") Admin admin);
}
package library.model.repository;

import java.util.List;

import library.model.entity.Placement;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODOS : 
 * - getAllPlacements() : rajouter LIMIT X, Y pour gérer la pagination
 * 
 * 
 * 
 */
public interface PlacementRepository  extends CrudRepository<Placement, Long> {
    @Transactional(readOnly = true)
    @Query("select pl from Placement pl")
    public List<Placement> getAllPlacements();

    @Transactional(readOnly = true)
    @Query("select pl from Placement pl where id = :id")
    public Placement findById(@Param("id") Long id);
}
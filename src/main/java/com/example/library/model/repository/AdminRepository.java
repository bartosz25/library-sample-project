package com.example.library.model.repository;

import java.util.List;

import com.example.library.model.entity.Admin;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODOS : 
 * - dans getAvailableAdmin() récupérer l'administrateur-chatteur qui a le moins de conversations : après voir si cela fonctionne avec ORDER BY RAND()
 */
public interface AdminRepository extends CrudRepository<Admin, Long> {
    @Transactional(readOnly = true)
    @Query("SELECT a FROM Admin a WHERE login = :login")
    public Admin loadByUsername(@Param("login") String login);

    @Query("SELECT a FROM Admin a")
    public List<Admin> getAvailableAdmin();
}
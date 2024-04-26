package com.barabanov.mandatory.model.dbms.dynamic.db.security.repository;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user.DbmsAdmin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.Optional;


public interface AdminRepository extends CrudRepository<DbmsAdmin, Long>
{
    Optional<DbmsAdmin> findByLogin(String login);
}

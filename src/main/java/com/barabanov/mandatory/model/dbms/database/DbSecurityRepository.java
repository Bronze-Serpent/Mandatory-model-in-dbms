package com.barabanov.mandatory.model.dbms.database;

import com.barabanov.mandatory.model.dbms.entity.DatabaseSecurity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface DbSecurityRepository extends JpaRepository<DatabaseSecurity, Long>
{
    Optional<DatabaseSecurity> findByName(String name);
}

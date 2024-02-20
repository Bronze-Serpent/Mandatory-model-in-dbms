package com.barabanov.mandatory.model.dbms.repository;

import com.barabanov.mandatory.model.dbms.entity.DatabaseSecurity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DbSecurityRepository extends JpaRepository<DatabaseSecurity, Long> {
}

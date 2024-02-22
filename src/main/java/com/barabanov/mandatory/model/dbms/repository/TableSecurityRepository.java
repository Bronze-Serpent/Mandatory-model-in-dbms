package com.barabanov.mandatory.model.dbms.repository;

import com.barabanov.mandatory.model.dbms.entity.TableSecurity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TableSecurityRepository extends JpaRepository<TableSecurity, Long>
{
    Optional<TableSecurity> findByName(String tableName);
}

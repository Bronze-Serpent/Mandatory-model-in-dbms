package com.barabanov.mandatory.model.dbms.repository;

import com.barabanov.mandatory.model.dbms.entity.ColumnSecurity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ColumnSecurityRepository extends JpaRepository<ColumnSecurity, Long>
{
    Optional<ColumnSecurity> findByName(String columnName);
}

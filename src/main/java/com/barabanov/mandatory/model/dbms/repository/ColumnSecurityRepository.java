package com.barabanov.mandatory.model.dbms.repository;

import com.barabanov.mandatory.model.dbms.entity.ColumnSecurity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ColumnSecurityRepository extends JpaRepository<ColumnSecurity, Long> {
}

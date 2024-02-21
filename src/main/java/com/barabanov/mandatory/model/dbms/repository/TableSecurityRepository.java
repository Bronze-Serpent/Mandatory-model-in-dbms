package com.barabanov.mandatory.model.dbms.repository;

import com.barabanov.mandatory.model.dbms.entity.TableSecurity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TableSecurityRepository extends JpaRepository<TableSecurity, Long> {
}

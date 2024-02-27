package com.barabanov.mandatory.model.dbms.dynamic.db.security.repository;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.ColumnSecurity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface ColumnSecurityRepository extends JpaRepository<ColumnSecurity, Long>
{
    @Query("select c " +
            "from ColumnSecurity c " +
            "inner join c.tableSecurity t " +
            "where t.id = :tableSecId and c.name = :name")
    Optional<ColumnSecurity> findByNameInTable(Long tableSecId, String name);
}

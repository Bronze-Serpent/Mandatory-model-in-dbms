package com.barabanov.mandatory.model.dbms.repository;

import com.barabanov.mandatory.model.dbms.entity.TableSecurity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface TableSecurityRepository extends JpaRepository<TableSecurity, Long>
{
    @Query("select t " +
            "from TableSecurity t " +
            "inner join t.databaseSecurity d " +
            "where d.id = :dbId and t.name = :tableName")
    Optional<TableSecurity> findByNameInDb(Long dbId, String tableName);
}

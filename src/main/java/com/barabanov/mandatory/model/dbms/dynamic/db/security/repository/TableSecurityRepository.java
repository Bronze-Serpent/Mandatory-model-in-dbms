package com.barabanov.mandatory.model.dbms.dynamic.db.security.repository;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.TableSecurity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface TableSecurityRepository extends JpaRepository<TableSecurity, Long>
{
    @Query("select t " +
            "from TableSecurity t " +
            "inner join t.databaseSecurity d " +
            "where d.id = :dbSecId and t.name = :tableName")
    Optional<TableSecurity> findByNameInDb(Long dbSecId, String tableName);


    @Query("select t " +
            "from TableSecurity t " +
            "inner join t.databaseSecurity d " +
            "where d.id = :dbSecId")
    List<TableSecurity> findAllInDb(Long dbSecId);
}

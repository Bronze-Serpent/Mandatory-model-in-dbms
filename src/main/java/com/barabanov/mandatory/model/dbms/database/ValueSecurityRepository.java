package com.barabanov.mandatory.model.dbms.database;

import com.barabanov.mandatory.model.dbms.entity.ValueSecurity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface ValueSecurityRepository extends JpaRepository<ValueSecurity, Long>
{
    @Query("select v " +
            "from ValueSecurity v " +
            "inner join v.columnSecurity c " +
            "where v.tupleId = :tupleId and c.id = :columnId")
    Optional<ValueSecurity> findByTupleIdAndColumnInTable(Long tupleId, Long columnId);
}

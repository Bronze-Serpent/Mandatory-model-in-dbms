package com.barabanov.mandatory.model.dbms.dynamic.db.security.repository;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.ValueSecurity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface ValueSecurityRepository extends JpaRepository<ValueSecurity, Long>
{
    @Query("select v " +
            "from ValueSecurity v " +
            "inner join v.columnSecurity c " +
            "where v.tupleId = :tupleId and c.id = :columnSecId")
    Optional<ValueSecurity> findByTupleIdAndColumnInTable(Long tupleId, Long columnSecId);
}

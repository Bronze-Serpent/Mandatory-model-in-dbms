package com.barabanov.mandatory.model.dbms.database;

import com.barabanov.mandatory.model.dbms.entity.TupleSecurity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface TupleSecurityRepository extends JpaRepository<TupleSecurity, Long>
{
    @Query("select t " +
            "from TupleSecurity t " +
            "inner join t.tableSecurity ts " +
            "where ts.id = :tableId and t.tupleId = :tupleId")
    Optional<TupleSecurity> findByTupleIdInTable(Long tableId, Long tupleId);


    @Query("delete " +
            "from TupleSecurity t " +
            "where t.tupleId = :tupleId")
    void deleteByTupleId(Long tupleId);
}

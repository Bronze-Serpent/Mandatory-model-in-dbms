package com.barabanov.mandatory.model.dbms.dynamic.db.security.repository;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.TupleSecurity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface TupleSecurityRepository extends JpaRepository<TupleSecurity, Long>
{
    @Query("select t " +
            "from TupleSecurity t " +
            "inner join t.tableSecurity ts " +
            "where ts.id = :tableSecId and t.tupleId = :tupleId")
    Optional<TupleSecurity> findByTupleIdInTable(Long tableSecId, Long tupleId);


    @Query("delete " +
            "from TupleSecurity t " +
            "where t.tupleId = :tupleId")
    void deleteByTupleId(Long tupleId);
}

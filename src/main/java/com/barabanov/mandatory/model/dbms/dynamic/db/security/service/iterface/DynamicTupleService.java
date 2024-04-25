package com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadTupleSecurityDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;
import org.springframework.jdbc.support.rowset.SqlRowSet;


public interface DynamicTupleService
{
    SqlRowSet executeSqlInDb(Long dbId, String sqlSelect);

    ReadTupleSecurityDto insertIntoDb(Long dbId, String securitySql);

    ReadTupleSecurityDto changeTupleSecLvl(
            Long tableId,
            Long tupleId,
            SecurityLevel newSecurityLvl
    );

    void deleteTupleInTable(Long tableId, Long tupleId);
}

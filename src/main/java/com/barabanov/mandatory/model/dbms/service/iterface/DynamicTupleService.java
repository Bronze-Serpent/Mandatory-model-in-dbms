package com.barabanov.mandatory.model.dbms.service.iterface;

import com.barabanov.mandatory.model.dbms.controller.dto.ReadTupleSecurityDto;
import com.barabanov.mandatory.model.dbms.entity.SecurityLevel;


public interface DynamicTupleService
{
    String getDataWithSecurityLvl(
            Long dbId,
            String sqlSelect,
            SecurityLevel securityLevel
    );

    ReadTupleSecurityDto insertIntoDb(Long dbId, String securitySql);

    ReadTupleSecurityDto changeTupleSecLvl(
            Long tableId,
            Long tupleId,
            SecurityLevel newSecurityLvl
    );

    void deleteTupleInTable(Long tableId, Long tupleId);
}

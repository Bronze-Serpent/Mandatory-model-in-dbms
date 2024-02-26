package com.barabanov.mandatory.model.dbms.service.iterface;

import com.barabanov.mandatory.model.dbms.entity.SecurityLevel;


public interface SecureDynamicTupleService
{
    String getDataWithSecurityLvl(Long dbId,
                                         String sqlSelect,
                                         SecurityLevel securityLevel);

    Long insertIntoDb(Long dbId, String securitySql);

    void changeTupleSecLvl(Long tableId,
                      Long tupleId,
                      SecurityLevel newSecurityLvl);

    void deleteTupleInTable(Long tableId, Long tupleId);
}

package com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadDbSecDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;


public interface DynamicDbService
{
    ReadDbSecDto createDb(String dbName, SecurityLevel securityLevel);

    ReadDbSecDto changeDbSecLvl(Long dbId, SecurityLevel newSecLevel);

    void deleteDb(Long dbId);
}

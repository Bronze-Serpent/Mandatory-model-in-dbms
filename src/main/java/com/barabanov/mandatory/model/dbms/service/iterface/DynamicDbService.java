package com.barabanov.mandatory.model.dbms.service.iterface;

import com.barabanov.mandatory.model.dbms.controller.dto.ReadDbSecDto;
import com.barabanov.mandatory.model.dbms.entity.SecurityLevel;


public interface DynamicDbService
{
    ReadDbSecDto createDb(String dbName, SecurityLevel securityLevel);

    ReadDbSecDto changeDbSecLvl(Long dbId, SecurityLevel newSecLevel);

    void deleteDb(Long dbId);
}

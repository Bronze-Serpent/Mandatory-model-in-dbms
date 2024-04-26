package com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadDbSecAdminDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadDbSecDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;

import java.util.List;


public interface DynamicDbService
{
    ReadDbSecAdminDto createDb(String dbName, SecurityLevel securityLevel);

    ReadDbSecAdminDto changeDbSecLvl(Long dbId, SecurityLevel newSecLevel);

    void deleteDb(Long dbId);

    List<ReadDbSecDto> getDatabasesList();
}

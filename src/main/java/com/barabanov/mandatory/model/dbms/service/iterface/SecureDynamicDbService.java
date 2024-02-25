package com.barabanov.mandatory.model.dbms.service.iterface;

import com.barabanov.mandatory.model.dbms.entity.SecurityLevel;


public interface SecureDynamicDbService
{
    void createDb(String dbName, SecurityLevel securityLevel);

    void changeDbSecLvl(Long dbId, SecurityLevel newSecLevel);

    void deleteDb(Long dbId);
}

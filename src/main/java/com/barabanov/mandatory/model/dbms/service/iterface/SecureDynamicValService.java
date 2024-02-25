package com.barabanov.mandatory.model.dbms.service.iterface;

import com.barabanov.mandatory.model.dbms.entity.SecurityLevel;


public interface SecureDynamicValService
{
    void changeValueSecLvl(Long tupleId,
                           Long columnId,
                           SecurityLevel newSecLvl);
}

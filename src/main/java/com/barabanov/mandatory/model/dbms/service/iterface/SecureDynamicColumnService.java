package com.barabanov.mandatory.model.dbms.service.iterface;

import com.barabanov.mandatory.model.dbms.entity.SecurityLevel;


public interface SecureDynamicColumnService
{
    void changeColumnSecLvl(Long columnId, SecurityLevel newSecLvl);
}

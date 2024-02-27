package com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadColumnSecDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;


public interface DynamicColumnService
{
    ReadColumnSecDto changeColumnSecLvl(Long columnId, SecurityLevel newSecLvl);
}

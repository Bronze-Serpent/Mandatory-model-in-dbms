package com.barabanov.mandatory.model.dbms.service.iterface;

import com.barabanov.mandatory.model.dbms.service.dto.ReadColumnSecDto;
import com.barabanov.mandatory.model.dbms.entity.SecurityLevel;


public interface DynamicColumnService
{
    ReadColumnSecDto changeColumnSecLvl(Long columnId, SecurityLevel newSecLvl);
}

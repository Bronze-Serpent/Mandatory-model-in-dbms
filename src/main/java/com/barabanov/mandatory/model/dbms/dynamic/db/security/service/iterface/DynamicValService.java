package com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadValueSecDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;


public interface DynamicValService
{
    ReadValueSecDto changeValueSecLvl(Long tupleId,
                                      Long columnId,
                                      SecurityLevel newSecLvl);
}

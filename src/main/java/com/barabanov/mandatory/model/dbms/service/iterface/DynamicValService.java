package com.barabanov.mandatory.model.dbms.service.iterface;

import com.barabanov.mandatory.model.dbms.service.dto.ReadValueSecDto;
import com.barabanov.mandatory.model.dbms.entity.SecurityLevel;


public interface DynamicValService
{
    ReadValueSecDto changeValueSecLvl(Long tupleId,
                                      Long columnId,
                                      SecurityLevel newSecLvl);
}

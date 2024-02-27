package com.barabanov.mandatory.model.dbms.service.iterface;

import com.barabanov.mandatory.model.dbms.controller.dto.ReadTableSecDto;
import com.barabanov.mandatory.model.dbms.dto.ColumnDesc;
import com.barabanov.mandatory.model.dbms.entity.SecurityLevel;

import java.util.List;


public interface DynamicTableService
{
    ReadTableSecDto createTableInDb(Long dbId,
                                    String tableName,
                                    List<ColumnDesc> columnsDesc,
                                    SecurityLevel securityLevel);

    ReadTableSecDto createTableInDb(Long dbId,
                                    String tableName,
                                    List<ColumnDesc> columnsDesc);

    ReadTableSecDto changeTableSecLvl(Long tableId, SecurityLevel newSecLevel);


    void dropTableInDb(Long tableId);
}

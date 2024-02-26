package com.barabanov.mandatory.model.dbms.service.iterface;

import com.barabanov.mandatory.model.dbms.dto.ColumnDesc;
import com.barabanov.mandatory.model.dbms.entity.SecurityLevel;

import java.util.List;


public interface SecureDynamicTableService
{
    void createTableInDb(Long dbId,
                         String tableName,
                         List<ColumnDesc> columnsDesc,
                         SecurityLevel securityLevel);

    void createTableInDb(Long dbId,
                         String tableName,
                         List<ColumnDesc> columnsDesc);

    void changeTableSecLvl(Long tableId, SecurityLevel newSecLevel);


    void dropTableInDb(Long tableId);
}

package com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadTableSecDto;
import com.barabanov.mandatory.model.dbms.secure.sql.dto.ColumnDesc;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;

import java.util.List;


public interface DynamicTableService
{

    List<ReadTableSecDto> getListOfTablesInDb(Long dbSecId);

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

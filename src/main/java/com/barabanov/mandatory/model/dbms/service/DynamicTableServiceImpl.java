package com.barabanov.mandatory.model.dbms.service;

import com.barabanov.mandatory.model.dbms.service.dto.ReadTableSecDto;
import com.barabanov.mandatory.model.dbms.dto.ColumnDesc;
import com.barabanov.mandatory.model.dbms.entity.ColumnSecurity;
import com.barabanov.mandatory.model.dbms.entity.DatabaseSecurity;
import com.barabanov.mandatory.model.dbms.entity.SecurityLevel;
import com.barabanov.mandatory.model.dbms.entity.TableSecurity;
import com.barabanov.mandatory.model.dbms.exception.DbNotFoundException;
import com.barabanov.mandatory.model.dbms.exception.TableNotFoundException;
import com.barabanov.mandatory.model.dbms.database.ColumnSecurityRepository;
import com.barabanov.mandatory.model.dbms.database.DbSecurityRepository;
import com.barabanov.mandatory.model.dbms.database.DynamicDbManager;
import com.barabanov.mandatory.model.dbms.database.TableSecurityRepository;
import com.barabanov.mandatory.model.dbms.mapper.TableSecurityMapper;
import com.barabanov.mandatory.model.dbms.service.iterface.DynamicTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Transactional
@Service
public class DynamicTableServiceImpl implements DynamicTableService
{
    private final DynamicDbManager dynamicDbManager;
    private final DbSecurityRepository dbSecurityRepository;
    private final TableSecurityRepository tableSecurityRepository;
    private final ColumnSecurityRepository columnSecurityRepository;
    private final TableSecurityMapper tableSecurityMapper;

    @Override
    public ReadTableSecDto createTableInDb(Long dbId,
                                           String tableName,
                                           List<ColumnDesc> columnsDesc,
                                           SecurityLevel securityLevel)
    {
        DatabaseSecurity dbSecurity = dbSecurityRepository.findById(dbId)
                .orElseThrow(() -> new DbNotFoundException(dbId, null));

        dynamicDbManager.createTable(dbSecurity.getName(), tableName, columnsDesc);
        TableSecurity tableSecurity = TableSecurity.builder()
                .name(tableName)
                .databaseSecurity(dbSecurity)
                .securityLevel(securityLevel)
                .build();
        createColumnSecurity(tableSecurity, columnsDesc);

        return tableSecurityMapper.toDto(tableSecurityRepository.save(tableSecurity));
    }


    @Override
    public ReadTableSecDto createTableInDb(Long dbId,
                                           String tableName,
                                           List<ColumnDesc> columnsDesc)
    {
        DatabaseSecurity dbSecurity = dbSecurityRepository.findById(dbId)
                .orElseThrow(() -> new DbNotFoundException(dbId, null));

        dynamicDbManager.createTable(dbSecurity.getName(), tableName, columnsDesc);
        TableSecurity tableSecurity = TableSecurity.builder()
                .name(tableName)
                .databaseSecurity(dbSecurity)
                .securityLevel(dbSecurity.getSecurityLevel())
                .build();
        createColumnSecurity(tableSecurity, columnsDesc);

        return tableSecurityMapper.toDto(tableSecurityRepository.save(tableSecurity));
    }


    @Override
    public ReadTableSecDto changeTableSecLvl(Long tableId, SecurityLevel newSecLevel)
    {
        TableSecurity tableSecurity = tableSecurityRepository.findById(tableId)
                .orElseThrow(() -> new TableNotFoundException(tableId, null));

        tableSecurity.setSecurityLevel(newSecLevel);

        return tableSecurityMapper.toDto(tableSecurityRepository.save(tableSecurity));
    }


    @Override
    public void dropTableInDb(Long tableId)
    {
        TableSecurity tableSecurity = tableSecurityRepository.findById(tableId)
                .orElseThrow(() -> new TableNotFoundException(tableId, null));

        dynamicDbManager.dropTable(tableSecurity.getDatabaseSecurity().getName(), tableSecurity.getName());

        tableSecurityRepository.delete(tableSecurity);
    }


    private void createColumnSecurity(TableSecurity tableSecurity, List<ColumnDesc> columnsDesc)
    {
        for (ColumnDesc columnDesc : columnsDesc)
        {
            ColumnSecurity columnSecurity = new ColumnSecurity();
            columnSecurity.setName(columnDesc.getName());
            columnSecurity.setTableSecurity(tableSecurity);
            if (columnDesc.getSecurityLevel() == null)
                columnSecurity.setSecurityLevel(tableSecurity.getSecurityLevel());
            else
                columnSecurity.setSecurityLevel(columnDesc.getSecurityLevel());

            columnSecurityRepository.save(columnSecurity);
        }
    }
}

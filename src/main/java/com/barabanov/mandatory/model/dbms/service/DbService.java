package com.barabanov.mandatory.model.dbms.service;

import com.barabanov.mandatory.model.dbms.dto.ColumnDesc;
import com.barabanov.mandatory.model.dbms.entity.DatabaseSecurity;
import com.barabanov.mandatory.model.dbms.entity.SecurityLevel;
import com.barabanov.mandatory.model.dbms.entity.TableSecurity;
import com.barabanov.mandatory.model.dbms.exception.DbNotFoundException;
import com.barabanov.mandatory.model.dbms.exception.TableNotFoundException;
import com.barabanov.mandatory.model.dbms.repository.DbManager;
import com.barabanov.mandatory.model.dbms.repository.DbSecurityRepository;
import com.barabanov.mandatory.model.dbms.repository.TableSecurityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Transactional
@Service
public class DbService
{
    private final DbManager dbManager;
    private final DbSecurityRepository dbSecurityRepository;
    private final TableSecurityRepository tableSecurityRepository;


    public void createDb(String dbName, SecurityLevel securityLevel)
    {
        dbManager.createDb(dbName);
        DatabaseSecurity createdDbSecurity = DatabaseSecurity.builder()
                .name(dbName)
                .securityLevel(securityLevel)
                .build();

        dbSecurityRepository.save(createdDbSecurity);
    }


    public void changeDbSecLvl(Long dbId, SecurityLevel securityLevel)
    {
        DatabaseSecurity dbSecurity = dbSecurityRepository.findById(dbId)
                .orElseThrow(() -> new DbNotFoundException(dbId));
        dbSecurityRepository.flush();
    }


    public void deleteDb(Long dbId)
    {
        DatabaseSecurity dbSecurity = dbSecurityRepository.findById(dbId)
                .orElseThrow(() -> new DbNotFoundException(dbId));

        dbManager.dropDb(dbSecurity.getName());
        dbSecurityRepository.delete(dbSecurity);
    }


    // TODO: 21.02.2024 Сделать аналогичные методы для создания таблиц / баз данных, но без securityLevel.
    //  В таком случае назначать securityLvl равным securityLvl родителя, а для БД - создателя.
    public void createTableInDb(Long dbId,
                                String tableName,
                                SecurityLevel securityLevel,
                                List<ColumnDesc> columnsDesc)
    {
        DatabaseSecurity dbSecurity = dbSecurityRepository.findById(dbId)
                .orElseThrow(() -> new DbNotFoundException(dbId));

        dbManager.createTable(dbSecurity.getName(), tableName, columnsDesc);
        TableSecurity tableSecurity = TableSecurity.builder()
                .name(tableName)
                .databaseSecurity(dbSecurity)
                .securityLevel(securityLevel)
                .build();

        tableSecurityRepository.save(tableSecurity);
    }


    public void createTableInDb(Long dbId,
                                String tableName,
                                List<ColumnDesc> columnsDesc)
    {
        DatabaseSecurity dbSecurity = dbSecurityRepository.findById(dbId)
                .orElseThrow(() -> new DbNotFoundException(dbId));

        dbManager.createTable(dbSecurity.getName(), tableName, columnsDesc);
        TableSecurity tableSecurity = TableSecurity.builder()
                .name(tableName)
                .databaseSecurity(dbSecurity)
                .securityLevel(dbSecurity.getSecurityLevel())
                .build();

        tableSecurityRepository.save(tableSecurity);
    }


    public void dropTableInDb(Long dbId, Long tableId)
    {
        DatabaseSecurity dbSecurity = dbSecurityRepository.findById(dbId)
                .orElseThrow(() -> new DbNotFoundException(dbId));

        TableSecurity tableSecurity = tableSecurityRepository.findById(tableId)
                .orElseThrow(() -> new TableNotFoundException(dbId, tableId));

        dbManager.dropTable(dbSecurity.getName(), tableSecurity.getName());

        tableSecurityRepository.delete(tableSecurity);
    }
    
}

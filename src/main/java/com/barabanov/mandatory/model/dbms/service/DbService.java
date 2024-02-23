package com.barabanov.mandatory.model.dbms.service;

import com.barabanov.mandatory.model.dbms.dto.ColumnDesc;
import com.barabanov.mandatory.model.dbms.dto.ParsedSecretSqlDto;
import com.barabanov.mandatory.model.dbms.dto.ValueSecurityInfo;
import com.barabanov.mandatory.model.dbms.entity.*;
import com.barabanov.mandatory.model.dbms.exception.ColumnNotFoundException;
import com.barabanov.mandatory.model.dbms.exception.DbNotFoundException;
import com.barabanov.mandatory.model.dbms.exception.TableNotFoundException;
import com.barabanov.mandatory.model.dbms.repository.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;


@RequiredArgsConstructor
@Transactional
@Service
public class DbService
{
    private final DynamicDbManager dynamicDbManager;
    private final DbSecurityRepository dbSecurityRepository;
    private final TableSecurityRepository tableSecurityRepository;
    private final ColumnSecurityRepository columnSecurityRepository;
    private final TupleSecurityRepository tupleSecurityRepository;
    private final ValueSecurityRepository valueSecurityRepository;
    private final SecuritySqlParser securitySqlParser;
    private final JsonFactory jsonFactory;


    public String getDataFromDbAsJson(Long dbId, String sqlSelect) throws IOException
    {
        DatabaseSecurity dbSecurity = dbSecurityRepository.findById(dbId)
                .orElseThrow(() -> new DbNotFoundException(dbId));

        SqlRowSet rowSet = dynamicDbManager.executeSqlInDb(dbSecurity.getName(), sqlSelect);
        SqlRowSetMetaData metaData = rowSet.getMetaData();

        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(stringWriter);
        jsonGenerator.useDefaultPrettyPrinter();

        jsonGenerator.writeStartObject();
        while (rowSet.next())
        {
            for (int columnCounter = 1; columnCounter <= metaData.getColumnCount(); columnCounter++)
            {
                jsonGenerator.writeObjectField(
                        metaData.getColumnName(columnCounter),
                        rowSet.getObject(columnCounter)
                );
            }
        }
        jsonGenerator.writeEndObject();

        jsonGenerator.close();
        String resultJson = stringWriter.toString();
        stringWriter.close();
        return resultJson;
    }


    public void insertIntoDb(Long dbId, String securitySql)
    {
        ParsedSecretSqlDto parsedSecretSqlDto = securitySqlParser.parse(securitySql);

        DatabaseSecurity dbSecurity = dbSecurityRepository.findById(dbId)
                .orElseThrow(() -> new DbNotFoundException(dbId));
        TableSecurity tableSecurity = tableSecurityRepository.findByNameInDb(dbSecurity.getId(), parsedSecretSqlDto.getTableName())
                .orElseThrow(() -> new TableNotFoundException(null, parsedSecretSqlDto.getTableName()));

        Long insertedTupleId = dynamicDbManager.insertTuple(dbSecurity.getName(), parsedSecretSqlDto.getSql());

        SecurityLevel rowSecurityLvl = parsedSecretSqlDto.getRowSecurityLvl();
        if (rowSecurityLvl != null)
            tupleSecurityRepository.save(new TupleSecurity(insertedTupleId, tableSecurity, rowSecurityLvl));

        for (ValueSecurityInfo valueSecurityInfo : parsedSecretSqlDto.getValueSecurityInfoList())
        {
            ColumnSecurity columnSecurity = columnSecurityRepository.findByNameInTable(tableSecurity.getId(), valueSecurityInfo.getColumnName())
                    .orElseThrow(() -> new ColumnNotFoundException(null, valueSecurityInfo.getColumnName()));
            ValueSecurity valueSecurity = new ValueSecurity(
                    insertedTupleId,
                    columnSecurity,
                    valueSecurityInfo.getSecurityLevel()
            );
            valueSecurityRepository.save(valueSecurity);
        }
    }


    public void createDb(String dbName, SecurityLevel securityLevel)
    {
        dynamicDbManager.createDb(dbName);
        DatabaseSecurity createdDbSecurity = DatabaseSecurity.builder()
                .name(dbName)
                .securityLevel(securityLevel)
                .build();

        dbSecurityRepository.save(createdDbSecurity);
    }


    public void createDb(String dbName)
    {
        createDb(dbName, SecurityLevel.OF_PARTICULAR_IMPORTANCE);
    }


    public void changeDbSecLvl(Long dbId, SecurityLevel newSecLevel)
    {
        DatabaseSecurity dbSecurity = dbSecurityRepository.findById(dbId)
                .orElseThrow(() -> new DbNotFoundException(dbId));

        dbSecurity.setSecurityLevel(newSecLevel);
        dbSecurityRepository.flush();
    }


    public void deleteDb(Long dbId)
    {
        DatabaseSecurity dbSecurity = dbSecurityRepository.findById(dbId)
                .orElseThrow(() -> new DbNotFoundException(dbId));

        dynamicDbManager.dropDb(dbSecurity.getName());
        dbSecurityRepository.delete(dbSecurity);
    }


    public void createTableInDb(Long dbId,
                                String tableName,
                                SecurityLevel securityLevel,
                                List<ColumnDesc> columnsDesc)
    {
        DatabaseSecurity dbSecurity = dbSecurityRepository.findById(dbId)
                .orElseThrow(() -> new DbNotFoundException(dbId));

        dynamicDbManager.createTable(dbSecurity.getName(), tableName, columnsDesc);
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

        dynamicDbManager.createTable(dbSecurity.getName(), tableName, columnsDesc);
        TableSecurity tableSecurity = TableSecurity.builder()
                .name(tableName)
                .databaseSecurity(dbSecurity)
                .securityLevel(dbSecurity.getSecurityLevel())
                .build();

        tableSecurityRepository.save(tableSecurity);
    }


    public void dropTableInDb(Long tableId)
    {
        TableSecurity tableSecurity = tableSecurityRepository.findById(tableId)
                .orElseThrow(() -> new TableNotFoundException(tableId, null));

        dynamicDbManager.dropTable(tableSecurity.getDatabaseSecurity().getName(), tableSecurity.getName());

        tableSecurityRepository.delete(tableSecurity);
    }


    public void changeTableSecLvl(Long tableId, SecurityLevel newSecLevel)
    {
        TableSecurity tableSecurity = tableSecurityRepository.findById(tableId)
                .orElseThrow(() -> new TableNotFoundException(tableId, null));

        tableSecurity.setSecurityLevel(newSecLevel);
        tableSecurityRepository.flush();
    }


    public void changeColumnSecLvl(Long columnId, SecurityLevel newSecLvl)
    {
        ColumnSecurity columnSecurity = columnSecurityRepository.findById(columnId)
                .orElseThrow(() -> new ColumnNotFoundException(columnId, null));

        columnSecurity.setSecurityLevel(newSecLvl);
        columnSecurityRepository.flush();
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

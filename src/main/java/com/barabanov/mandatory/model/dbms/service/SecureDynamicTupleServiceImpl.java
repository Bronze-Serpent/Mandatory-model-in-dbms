package com.barabanov.mandatory.model.dbms.service;

import com.barabanov.mandatory.model.dbms.database.*;
import com.barabanov.mandatory.model.dbms.dto.ParsedSecretSqlDto;
import com.barabanov.mandatory.model.dbms.dto.ValueSecurityInfo;
import com.barabanov.mandatory.model.dbms.entity.*;
import com.barabanov.mandatory.model.dbms.exception.ColumnNotFoundException;
import com.barabanov.mandatory.model.dbms.exception.ConversionRowSetException;
import com.barabanov.mandatory.model.dbms.exception.DbNotFoundException;
import com.barabanov.mandatory.model.dbms.exception.TableNotFoundException;
import com.barabanov.mandatory.model.dbms.service.iterface.SecureDynamicTupleService;
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
public class SecureDynamicTupleServiceImpl implements SecureDynamicTupleService
{
    private final DynamicDbManager dynamicDbManager;
    private final DbSecurityRepository dbSecurityRepository;
    private final TableSecurityRepository tableSecurityRepository;
    private final ColumnSecurityRepository columnSecurityRepository;
    private final TupleSecurityRepository tupleSecurityRepository;
    private final ValueSecurityRepository valueSecurityRepository;
    private final SecuritySqlParser securitySqlParser;
    private final JsonFactory jsonFactory;


    @Override
    public String getDataWithSecurityLvl(Long dbId,
                                         String sqlSelect,
                                         SecurityLevel securityLevel)
    {
        DatabaseSecurity dbSecurity = dbSecurityRepository.findById(dbId)
                .orElseThrow(() -> new DbNotFoundException(dbId, null));

        SqlRowSet rowSet = dynamicDbManager.executeSqlInDb(dbSecurity.getName(), sqlSelect);
        SqlRowSetMetaData metaData = rowSet.getMetaData();

        try(StringWriter stringWriter = new StringWriter();
            JsonGenerator jsonGenerator = jsonFactory.createGenerator(stringWriter))
        {
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
            return stringWriter.toString();
        } catch (IOException e)
        {
            throw new ConversionRowSetException(e);
        }

    }


    @Override
    public Long insertIntoDb(Long dbId, String securitySql)
    {
        ParsedSecretSqlDto parsedSecretSqlDto = securitySqlParser.parse(securitySql);

        DatabaseSecurity dbSecurity = dbSecurityRepository.findById(dbId)
                .orElseThrow(() -> new DbNotFoundException(dbId, null));
        TableSecurity tableSecurity = tableSecurityRepository.findByNameInDb(dbSecurity.getId(), parsedSecretSqlDto.getTableName())
                .orElseThrow(() -> new TableNotFoundException(null, parsedSecretSqlDto.getTableName()));

        Long insertedTupleId = dynamicDbManager.insertTuple(dbSecurity.getName(), parsedSecretSqlDto.getSql());

        createSecurityRecords(
                parsedSecretSqlDto.getRowSecurityLvl(),
                insertedTupleId,
                tableSecurity,
                parsedSecretSqlDto.getValueSecurityInfoList()
                );

        return insertedTupleId;
    }


    @Override
    public void changeTupleSecLvl(Long tableId,
                                  Long tupleId,
                                  SecurityLevel newSecurityLvl)
    {
        TableSecurity tableSecurity = tableSecurityRepository.findById(tableId)
                .orElseThrow(() -> new TableNotFoundException(tableId, null));

        TupleSecurity tupleSecurity = tupleSecurityRepository.findByTupleIdInTable(tableId, tupleId)
                .orElseGet(() -> TupleSecurity.builder()
                        .tupleId(tupleId)
                        .tableSecurity(tableSecurity)
                        .build());

        tupleSecurity.setSecurityLevel(newSecurityLvl);
    }


    @Override
    public void deleteTupleInTable(Long tableId, Long tupleId)
    {
        TableSecurity tableSecurity = tableSecurityRepository.findById(tableId)
                .orElseThrow(() -> new TableNotFoundException(tableId, null));
        dynamicDbManager.deleteTuple(
                tableSecurity.getDatabaseSecurity().getName(),
                tableSecurity.getName(),
                tupleId);

        tupleSecurityRepository.deleteByTupleId(tupleId);
    }


    private void createSecurityRecords(SecurityLevel rowSecurityLvl,
                                       Long insertedTupleId,
                                       TableSecurity tableSecurity,
                                       List<ValueSecurityInfo> valueSecurityInfoList)
    {
        if (rowSecurityLvl != null)
            tupleSecurityRepository.save(new TupleSecurity(insertedTupleId, tableSecurity, rowSecurityLvl));

        for (ValueSecurityInfo valueSecurityInfo : valueSecurityInfoList)
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
}
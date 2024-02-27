package com.barabanov.mandatory.model.dbms.service;

import com.barabanov.mandatory.model.dbms.controller.dto.ReadTupleSecurityDto;
import com.barabanov.mandatory.model.dbms.database.*;
import com.barabanov.mandatory.model.dbms.dto.ParsedSecretSqlDto;
import com.barabanov.mandatory.model.dbms.dto.ValueSecurityInfo;
import com.barabanov.mandatory.model.dbms.entity.*;
import com.barabanov.mandatory.model.dbms.exception.ColumnNotFoundException;
import com.barabanov.mandatory.model.dbms.exception.ConversionRowSetException;
import com.barabanov.mandatory.model.dbms.exception.DbNotFoundException;
import com.barabanov.mandatory.model.dbms.exception.TableNotFoundException;
import com.barabanov.mandatory.model.dbms.mapper.TupleSecurityMapper;
import com.barabanov.mandatory.model.dbms.service.iterface.DynamicTupleService;
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
public class DynamicTupleServiceImpl implements DynamicTupleService
{
    private final DynamicDbManager dynamicDbManager;
    private final DbSecurityRepository dbSecurityRepository;
    private final TableSecurityRepository tableSecurityRepository;
    private final ColumnSecurityRepository columnSecurityRepository;
    private final TupleSecurityRepository tupleSecurityRepository;
    private final ValueSecurityRepository valueSecurityRepository;
    private final SecuritySqlParser securitySqlParser;
    private final JsonFactory jsonFactory;
    private final TupleSecurityMapper tupleSecurityMapper;

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
    public ReadTupleSecurityDto insertIntoDb(Long dbId, String securitySql)
    {
        ParsedSecretSqlDto parsedSecretSqlDto = securitySqlParser.parse(securitySql);

        DatabaseSecurity dbSecurity = dbSecurityRepository.findById(dbId)
                .orElseThrow(() -> new DbNotFoundException(dbId, null));
        TableSecurity tableSecurity = tableSecurityRepository.findByNameInDb(dbSecurity.getId(), parsedSecretSqlDto.getTableName())
                .orElseThrow(() -> new TableNotFoundException(null, parsedSecretSqlDto.getTableName()));

        Long insertedTupleId = dynamicDbManager.insertTuple(dbSecurity.getName(), parsedSecretSqlDto.getSql());

        TupleSecurity tupleSecEntity = createSecurityRecords(
                parsedSecretSqlDto.getRowSecurityLvl(),
                insertedTupleId,
                tableSecurity,
                parsedSecretSqlDto.getValueSecurityInfoList()
        );

        return tupleSecurityMapper.toDto(tupleSecEntity);
    }


    @Override
    public ReadTupleSecurityDto changeTupleSecLvl(Long tableId,
                                                  Long tupleId,
                                                  SecurityLevel newSecurityLvl)
    {
        // TODO: 27.02.2024 можно сделать проверку, если новый уровень безопасности равен уровню безопасности таблицы, то просто удалять запись
        TableSecurity tableSecurity = tableSecurityRepository.findById(tableId)
                .orElseThrow(() -> new TableNotFoundException(tableId, null));

        TupleSecurity tupleSecurity = tupleSecurityRepository.findByTupleIdInTable(tableId, tupleId)
                .orElseGet(() -> TupleSecurity.builder()
                        .tupleId(tupleId)
                        .tableSecurity(tableSecurity)
                        .build());

        tupleSecurity.setSecurityLevel(newSecurityLvl);

        return tupleSecurityMapper.toDto(tupleSecurity);
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


    private TupleSecurity createSecurityRecords(SecurityLevel rowSecurityLvl,
                                       Long insertedTupleId,
                                       TableSecurity tableSecurity,
                                       List<ValueSecurityInfo> valueSecurityInfoList)
    {
        TupleSecurity tupleSecurity = new TupleSecurity(insertedTupleId, tableSecurity, rowSecurityLvl);
        if (rowSecurityLvl != null)
            tupleSecurityRepository.save(tupleSecurity);

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

        return tupleSecurity;
    }
}
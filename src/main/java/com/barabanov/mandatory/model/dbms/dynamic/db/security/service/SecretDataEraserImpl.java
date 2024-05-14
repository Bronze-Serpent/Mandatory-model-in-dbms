package com.barabanov.mandatory.model.dbms.dynamic.db.security.service;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadColumnSecDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadDbSecAdminDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadDbSecDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadTableSecDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.DatabaseSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user.DbmsAdmin;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.ColumnSecurityRepository;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.TupleSecurityRepository;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.ValueSecurityRepository;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface.SecretDataEraser;
import com.barabanov.mandatory.model.dbms.secure.sql.exception.ConversionRowSetException;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RequiredArgsConstructor

@Service
public class SecretDataEraserImpl implements SecretDataEraser
{
    private static final String ID_IN_A_DYNAMIC_DATABASE = "id";

    private final JsonFactory jsonFactory;
    private final TupleSecurityRepository tupleSecurityRepository;
    private final ValueSecurityRepository valueSecurityRepository;


// Получается, что данный метод И секретные данные затирает И преобразует их в json. Лучше бы чтобы он только затирал,
// а преобразованием в json занимался другой метод. Однако sqlRowSet не подлежит изменению. Новый создать очень сложно.
// Единственный вариант, что я вижу - преобразовать сначала весь набор данных в json. Потом из этого json затирать секретные данные.
// Получается сложная логика так что пока что эти операции 2 в 1.
    @Override
    public String eraseRowSetAccordingToSecurityLvl(Long tableSecId, SqlRowSet rowSet, SecurityLevel securityLevel)
    {
        SqlRowSetMetaData metaData = rowSet.getMetaData();

        try(StringWriter stringWriter = new StringWriter())
        {
            JsonGenerator jsonGenerator = jsonFactory.createGenerator(stringWriter);
            jsonGenerator.useDefaultPrettyPrinter();

            jsonGenerator.writeStartArray();
            while (rowSet.next())
            {
                Long tupleId = rowSet.getLong(ID_IN_A_DYNAMIC_DATABASE);
                if (!isThereAccessToTuple(tableSecId, tupleId, securityLevel))
                    continue;

                jsonGenerator.writeStartObject();
                for (int columnCounter = 1; columnCounter <= metaData.getColumnCount(); columnCounter++)
                {
                    String columnName = metaData.getColumnName(columnCounter);
                    Object columnVal = null;
                    if (isThereAccessToValue(tupleId, columnName, securityLevel))
                        columnVal = rowSet.getObject(columnCounter);

                    jsonGenerator.writeObjectField(columnName, columnVal);
                }
                jsonGenerator.writeEndObject();
            }
            jsonGenerator.writeEndArray();

            jsonGenerator.close();
            return stringWriter.toString();
        } catch (IOException e)
        {
            throw new ConversionRowSetException(e);
        }
    }


    @Override
    public List<ReadColumnSecDto> eraseColumnsAccordingToSecurityLvl(List<ReadColumnSecDto> columnDtos, SecurityLevel securityLevel)
    {
        return columnDtos.stream()
                .filter(columnDto -> columnDto.getSecurityLevel().getImportantLvl() <= securityLevel.getImportantLvl())
                .toList();
    }


    @Override
    public List<ReadTableSecDto> eraseTablesAccordingToSecurityLvl(List<ReadTableSecDto> tableDtos, SecurityLevel securityLevel)
    {
        return tableDtos.stream()
                .filter(tableDto -> tableDto.getSecurityLevel().getImportantLvl() <= securityLevel.getImportantLvl())
                .toList();
    }


    @Override
    public List<ReadDbSecDto> eraseDatabasesAccordingToSecurityLvl(List<ReadDbSecDto> databaseDtos, SecurityLevel securityLevel)
    {
        return databaseDtos.stream()
                .filter(databaseDto -> databaseDto.getSecurityLevel().getImportantLvl() <= securityLevel.getImportantLvl())
                .toList();
    }

    @Override
    public List<ReadDbSecAdminDto> eraseDatabasesAccordingToAdminLinks(List<ReadDbSecAdminDto> allDatabases, DbmsAdmin dbmsAdmin)
    {
        Set<String> linkWithAdminDatabases = dbmsAdmin.getDatabases()
                .stream()
                .map(DatabaseSecurity::getName)
                .collect(Collectors.toSet());
        linkWithAdminDatabases.addAll(dbmsAdmin.getAdministeredDatabases()
                .stream()
                .map(DatabaseSecurity::getName)
                .collect(Collectors.toSet()));

        return allDatabases.stream()
                .filter(dbDto -> linkWithAdminDatabases.contains(dbDto.getName()))
                .toList();
    }


    private boolean isThereAccessToTuple(Long tableSecId, Long tupleId, SecurityLevel securityLevel)
    {
        return tupleSecurityRepository.findByTupleIdInTable(tableSecId, tupleId)
                .map(tupleSecurity -> tupleSecurity.getSecurityLevel().getImportantLvl() <= securityLevel.getImportantLvl())
                .orElse(true);
    }


    private boolean isThereAccessToValue(Long tupleId, String columnName, SecurityLevel securityLevel)
    {
        return valueSecurityRepository.findByTupleIdAndColumnName(tupleId, columnName)
                .map(valueSecurity -> valueSecurity.getSecurityLevel().getImportantLvl() <= securityLevel.getImportantLvl())
                .orElse(true);
    }

}

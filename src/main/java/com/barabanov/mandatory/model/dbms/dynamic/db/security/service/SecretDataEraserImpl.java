package com.barabanov.mandatory.model.dbms.dynamic.db.security.service;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.ColumnSecurityRepository;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.TupleSecurityRepository;
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


@RequiredArgsConstructor

@Service
public class SecretDataEraserImpl implements SecretDataEraser
{
    private static final String ID_IN_A_DYNAMIC_DATABASE = "id";

    private final JsonFactory jsonFactory;
    private final TupleSecurityRepository tupleSecurityRepository;
    private final ColumnSecurityRepository columnSecurityRepository;


// Получается, что данный метод И секретные данные затирает И преобразует их в json. Лучше бы чтобы он только затирал,
// а преобразованием в json занимался другой метод. Однако sqlRowSet не подлежит изменению. Новый создать очень сложно.
// Единственный вариант, что я вижу - преобразовать сначала весь набор данных в json. Потом из этого json затирать секретные данные.
// Получается сложная логика так что пока что эти операции 2 в 1.
    @Override
    public String eraseDataAccordingToSecurityLvl(Long tableSecId, SqlRowSet rowSet, SecurityLevel securityLevel)
    {
        SqlRowSetMetaData metaData = rowSet.getMetaData();

        try(StringWriter stringWriter = new StringWriter())
        {
            JsonGenerator jsonGenerator = jsonFactory.createGenerator(stringWriter);
            jsonGenerator.useDefaultPrettyPrinter();

            jsonGenerator.writeStartArray();
            while (rowSet.next())
            {
                if (!isThereAccessToTuple(tableSecId,
                        rowSet.getLong(ID_IN_A_DYNAMIC_DATABASE),
                        securityLevel))
                    continue;

                jsonGenerator.writeStartObject();
                for (int columnCounter = 1; columnCounter <= metaData.getColumnCount(); columnCounter++)
                {
                    String columnName = metaData.getColumnName(columnCounter);
                    Object columnVal = null;
                    if (isThereAccessToColumn(tableSecId, columnName, securityLevel))
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


    private boolean isThereAccessToTuple(Long tableSecId, Long tupleId, SecurityLevel securityLevel)
    {
        return tupleSecurityRepository.findByTupleIdInTable(tableSecId, tupleId)
                .map(tupleSecurity -> tupleSecurity.getSecurityLevel().getImportantLvl() < securityLevel.getImportantLvl())
                .orElse(false);
    }


    private boolean isThereAccessToColumn(Long tableSecId, String columnName, SecurityLevel securityLevel)
    {
        return columnSecurityRepository.findByNameInTable(tableSecId, columnName)
                .map(columnSecurity -> columnSecurity.getSecurityLevel().getImportantLvl() > securityLevel.getImportantLvl())
                .orElse(false);
    }

}

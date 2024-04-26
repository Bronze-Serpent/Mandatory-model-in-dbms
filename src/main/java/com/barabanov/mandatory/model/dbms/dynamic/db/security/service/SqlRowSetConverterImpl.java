package com.barabanov.mandatory.model.dbms.dynamic.db.security.service;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface.SqlRowSetConverter;
import com.barabanov.mandatory.model.dbms.secure.sql.exception.ConversionRowSetException;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;


@Service
@RequiredArgsConstructor
public class SqlRowSetConverterImpl implements SqlRowSetConverter
{
    private final JsonFactory jsonFactory;


    @Override
    public String convertToJson(SqlRowSet rowSet)
    {
        SqlRowSetMetaData metaData = rowSet.getMetaData();

        try(StringWriter stringWriter = new StringWriter())
        {
            JsonGenerator jsonGenerator = jsonFactory.createGenerator(stringWriter);
            jsonGenerator.useDefaultPrettyPrinter();

            jsonGenerator.writeStartArray();
            while (rowSet.next())
            {
                jsonGenerator.writeStartObject();
                for (int columnCounter = 1; columnCounter <= metaData.getColumnCount(); columnCounter++)
                    jsonGenerator.writeObjectField(metaData.getColumnName(columnCounter),
                            rowSet.getObject(columnCounter));
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
}

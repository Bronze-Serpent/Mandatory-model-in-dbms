package com.barabanov.mandatory.model.dbms.secure.sql.exception;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.exception.DynamicDatabaseNotFoundException;
import lombok.RequiredArgsConstructor;

import java.io.IOException;


@RequiredArgsConstructor
public class ConversionRowSetException extends DynamicDatabaseNotFoundException
{
    private final IOException nestedException;

    @Override
    public String toString()
    {
        return "Exception during conversation SqlRowSet to Json. Nested IOException: " + nestedException.toString();
    }
}

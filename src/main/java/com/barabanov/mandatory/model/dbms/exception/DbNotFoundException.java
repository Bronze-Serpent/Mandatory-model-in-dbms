package com.barabanov.mandatory.model.dbms.exception;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class DbNotFoundException extends RuntimeException
{
    private final Long dbId;
    private final String dbName;


    @Override
    public String toString()
    {
        String message = "Database with ";
        if (dbId != null)
            message += "id: " + dbId + " ";
        if (dbName != null)
            message += "database name: " + dbName;
        message += "not found.";

        return message;
    }
}

package com.barabanov.mandatory.model.dbms.dynamic.db.security.exception;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class TableNotFoundException extends DynamicDatabaseNotFoundException
{
    private final Long tableId;

    private final String tableName;

    @Override
    public String toString()
    {
        String message = "Table with ";
        if (tableId != null)
            message += "id: " + tableId + " ";
        if (tableName != null)
            message += "columnName: " + tableName;
        message += " not found.";

        return message;
    }
}

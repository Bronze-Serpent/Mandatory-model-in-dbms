package com.barabanov.mandatory.model.dbms.exception;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class ColumnNotFoundException extends RuntimeException
{
    private final Long columnId;

    private final String columnName;

    @Override
    public String toString()
    {
        String message = "Column with ";
        if (columnId != null)
            message += "id: " + columnId + " ";
        if (columnName != null)
            message += "columnName: " + columnName;
        message += "not found.";

        return message;
    }
}

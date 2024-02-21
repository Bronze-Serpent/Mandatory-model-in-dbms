package com.barabanov.mandatory.model.dbms.exception;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class TableNotFoundException extends RuntimeException
{
    private final Long dbId;
    private final Long tableId;


    @Override
    public String toString()
    {
        return "Table with id: " + tableId + " not found in database with id: " + dbId;
    }
}

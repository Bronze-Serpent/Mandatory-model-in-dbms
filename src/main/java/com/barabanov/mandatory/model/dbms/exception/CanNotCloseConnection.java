package com.barabanov.mandatory.model.dbms.exception;

import lombok.RequiredArgsConstructor;

import java.sql.SQLException;


@RequiredArgsConstructor
public class CanNotCloseConnection extends RuntimeException
{
    private final SQLException sqlException;


    @Override
    public String toString()
    {
        return "Exception when closing connection. Nested exception: " + sqlException;
    }
}

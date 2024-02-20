package com.barabanov.mandatory.model.dbms.exception;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class DbNotFoundException extends RuntimeException
{
    private final Long dbId;


    @Override
    public String toString()
    {
        return "Database with id: " + dbId + " not found.";
    }
}

package com.barabanov.mandatory.model.dbms.exception;

import lombok.RequiredArgsConstructor;

import java.io.IOException;


@RequiredArgsConstructor
public class ConversionRowSetException extends RuntimeException
{
    private final IOException nestedException;

    @Override
    public String toString()
    {
        return "Exception during conversation SqlRowSet to Json. Nested IOException: " + nestedException.toString();
    }
}

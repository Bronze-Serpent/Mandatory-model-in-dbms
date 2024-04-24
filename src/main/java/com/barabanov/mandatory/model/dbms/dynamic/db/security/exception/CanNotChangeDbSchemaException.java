package com.barabanov.mandatory.model.dbms.dynamic.db.security.exception;

import lombok.Getter;


@Getter
public class CanNotChangeDbSchemaException extends RuntimeException
{
    private final Long dbId;

    public CanNotChangeDbSchemaException(Long dbId, String message) {
        super(message);
        this.dbId = dbId;
    }
}

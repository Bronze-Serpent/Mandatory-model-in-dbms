package com.barabanov.mandatory.model.dbms.dynamic.db.security.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum SecurityLevel
{
    NOT_SECRET(0),
    SECRET(1),
    TOP_SECRET(2),
    OF_PARTICULAR_IMPORTANCE(3);

    public static SecurityLevel fromString(String text)
    {
        return SecurityLevel.valueOf(text.replaceAll(" ", "_").toUpperCase());
    }

    private final int importantLvl;
}

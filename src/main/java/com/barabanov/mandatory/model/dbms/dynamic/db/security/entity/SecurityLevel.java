package com.barabanov.mandatory.model.dbms.dynamic.db.security.entity;


public enum SecurityLevel
{
    NOT_SECRET,
    SECRET,
    TOP_SECRET,
    OF_PARTICULAR_IMPORTANCE;

    public static SecurityLevel fromString(String text)
    {
        return SecurityLevel.valueOf(text.replaceAll(" ", "_").toUpperCase());
    }
}

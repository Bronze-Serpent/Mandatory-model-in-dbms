package com.barabanov.mandatory.model.dbms.sql;

public interface SqlParser
{
    ParsedSqlDto parseSelectQuery(String selectSql);
}

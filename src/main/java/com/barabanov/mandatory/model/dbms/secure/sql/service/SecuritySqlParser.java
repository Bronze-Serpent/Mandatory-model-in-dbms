package com.barabanov.mandatory.model.dbms.secure.sql.service;


import com.barabanov.mandatory.model.dbms.secure.sql.dto.ParsedSecretSqlDto;


public interface SecuritySqlParser
{
    ParsedSecretSqlDto parse(String securitySql);
}

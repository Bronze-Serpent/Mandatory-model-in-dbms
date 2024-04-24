package com.barabanov.mandatory.model.dbms.secure.sql.service;


import com.barabanov.mandatory.model.dbms.secure.sql.dto.ParsedSecureSqlDto;


public interface SecuritySqlParser
{
    ParsedSecureSqlDto parse(String securitySql);
}

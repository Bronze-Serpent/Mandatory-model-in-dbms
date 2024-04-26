package com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface;

import org.springframework.jdbc.support.rowset.SqlRowSet;


public interface SqlRowSetConverter
{
    String convertToJson(SqlRowSet rowSet);
}

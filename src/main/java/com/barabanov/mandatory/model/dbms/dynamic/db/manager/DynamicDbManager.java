package com.barabanov.mandatory.model.dbms.dynamic.db.manager;

import com.barabanov.mandatory.model.dbms.secure.sql.dto.ColumnDesc;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.List;

public interface DynamicDbManager
{
    SqlRowSet executeSqlInDb(String dbName, String sql);

    void deleteTuple(String dbName, String tableName, Long tupleId);

    Long insertTuple(String dbName, String insertSql);

    void createTable(String dbName, String tableName, List<ColumnDesc> columnsDesc);

    void dropTable(String dbName, String tableName);

    void createDb(String dbName);

    void dropDb(String dbName);
}

package com.barabanov.mandatory.model.dbms.repository;

import com.barabanov.mandatory.model.dbms.dto.ColumnDesc;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlRowSetResultSetExtractor;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class DynamicDbManager
{
    private final static String RETURN_ID_SQL_COMMAND = "RETURNING id";

    private final JdbcTemplate jdbcTemplate;
    private final Environment environment;


    public SqlRowSet executeSqlInDb(String dbName, String sql)
    {
        JdbcTemplate dynamicTemplate = createJdbcTemplateFor(dbName);
        SqlRowSet rowSet = dynamicTemplate.query(sql, new SqlRowSetResultSetExtractor());
        closeConnection(dynamicTemplate);

        return rowSet;
    }


    @SuppressWarnings("ConstantConditions")
    public Long insertTuple(String dbName, String insertSql)
    {
        JdbcTemplate dynamicTemplate = createJdbcTemplateFor(dbName);

        String insertSqlReturningId = insertSql.replace(';', ' ') + RETURN_ID_SQL_COMMAND + ';';

        SqlRowSet createdIdRowSet = dynamicTemplate.query(insertSqlReturningId, new SqlRowSetResultSetExtractor());
        closeConnection(dynamicTemplate);

        createdIdRowSet.next();
        return createdIdRowSet.getLong(1);
    }


    public void createTable(String dbName, String tableName, List<ColumnDesc> columnsDesc)
    {
        StringBuilder sqlBuilder = new StringBuilder("CREATE TABLE " + tableName);
        sqlBuilder.append("\n(\n");
        for (int i = 0; i < columnsDesc.size(); i++)
        {
            sqlBuilder.append(columnsDesc.get(i).getName())
                    .append(" ")
                    .append(columnsDesc.get(i).getType())
                    .append(" ")
                    .append(String.join(" ", columnsDesc.get(i).getConstraints()));

            if (i != columnsDesc.size() - 1)
                sqlBuilder.append(",\n");
        }
        sqlBuilder.append(");");

        JdbcTemplate dynamicTemplate = createJdbcTemplateFor(dbName);
        dynamicTemplate.execute(sqlBuilder.toString());
        closeConnection(dynamicTemplate);
    }


    public void dropTable(String dbName, String tableName)
    {
        JdbcTemplate dynamicTemplate = createJdbcTemplateFor(dbName);

        dynamicTemplate.execute("DROP TABLE " + tableName + ";");

        closeConnection(dynamicTemplate);
    }

    public void createDb(String dbName)
    {
        jdbcTemplate.execute("CREATE DATABASE " + dbName + ";");
    }

    public void dropDb(String dbName)
    {
        jdbcTemplate.execute("DROP DATABASE " + dbName + ";");
    }


    @SuppressWarnings("ConstantConditions")
    private JdbcTemplate createJdbcTemplateFor(String dbName)
    {
        SingleConnectionDataSource ds = new SingleConnectionDataSource();

        ds.setDriverClassName(environment.getProperty("spring.datasource.driver-class-name"));
        ds.setUsername(environment.getProperty("spring.datasource.username"));
        ds.setPassword(environment.getProperty("spring.datasource.password"));

        String currUrl = environment.getProperty("spring.datasource.url");
        int lastSlashIndex = currUrl.lastIndexOf('/');
        String newUrl = currUrl.substring(0, lastSlashIndex + 1) + dbName;
        ds.setUrl(newUrl);

        return new JdbcTemplate(ds);
    }


    /**
     * Предполагается, что в объекте JdbcTemplate будет объект именно SingleConnectionDataSource
     * т.к. у него всего одно соединение, которое и закрывает метод.
     *
     * Можно у объекта DataSource получить Connection и закрыть его. С SingleConnectionDataSource работа метода
     * будет аналогична, но в других реализациях поведение не будет являться ожидаемым, поэтому используется явное приведение типа.
     */
    @SuppressWarnings("ConstantConditions")
    private void closeConnection(JdbcTemplate template)
    {
        SingleConnectionDataSource dataSource = (SingleConnectionDataSource) template.getDataSource();

        dataSource.close();
    }
}

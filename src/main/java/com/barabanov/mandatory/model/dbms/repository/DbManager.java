package com.barabanov.mandatory.model.dbms.repository;

import com.barabanov.mandatory.model.dbms.dto.ColumnDesc;
import com.barabanov.mandatory.model.dbms.exception.CanNotCloseConnection;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


@RequiredArgsConstructor
@Service
public class DbManager
{
    private final JdbcTemplate jdbcTemplate;

    private final Environment environment;


    public void createDb(String dbName)
    {
        jdbcTemplate.execute("CREATE DATABASE " + dbName);
    }

    public void dropDb(String dbName)
    {
        jdbcTemplate.execute("DROP DATABASE " + dbName);
    }


    public void createTable(String dbName, String tableName, List<ColumnDesc> columnsDesc)
    {
        JdbcTemplate dynamicTemplate = createJdbcTemplateFor(dbName);

        StringBuilder sqlBuilder = new StringBuilder("CREATE TABLE " + tableName);
        sqlBuilder.append("\n(\n");
        for (ColumnDesc columnDesc : columnsDesc)
        {
            sqlBuilder.append(columnDesc.getName())
                    .append(" ")
                    .append(columnDesc.getType())
                    .append(" ")
                    .append(columnDesc.getConstraints())
                    .append(",\n");
        }
        sqlBuilder.append(");");

        dynamicTemplate.execute(sqlBuilder.toString());

        closeConnection(dynamicTemplate);
    }


    public void dropTable(String dbName, String tableName)
    {
        JdbcTemplate dynamicTemplate = createJdbcTemplateFor(dbName);

        dynamicTemplate.execute("DROP TABLE " + tableName + ";");

        closeConnection(dynamicTemplate);
    }


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


    private void closeConnection(JdbcTemplate template)
    {
        try {
            DataSource dataSource = template.getDataSource();
            Connection connection = dataSource.getConnection();

            connection.close();

        } catch (SQLException e)
        {
            throw new CanNotCloseConnection(e);
        }
    }
}

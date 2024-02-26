package com.barabanov.mandatory.model.dbms.db;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;


@RequiredArgsConstructor
public class DynamicDbWorker
{
    private final Environment environment;


    @SuppressWarnings("ConstantConditions")
    public JdbcTemplate createJdbcTemplateFor(String dbName)
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


    @SuppressWarnings("ConstantConditions")
    public void closeConnection(JdbcTemplate template)
    {
        SingleConnectionDataSource dataSource = (SingleConnectionDataSource) template.getDataSource();

        dataSource.close();
    }
}

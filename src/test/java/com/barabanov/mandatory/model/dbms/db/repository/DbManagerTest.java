package com.barabanov.mandatory.model.dbms.db.repository;

import com.barabanov.mandatory.model.dbms.db.ContainerTestBase;
import com.barabanov.mandatory.model.dbms.repository.DbManager;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlRowSetResultSetExtractor;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RequiredArgsConstructor
public class DbManagerTest extends ContainerTestBase
{
    private final DbManager dbManager;

    private final EntityManager entityManager;
    private final Environment environment;


    @Test
    public void shouldCreateDatabase()
    {
        String dbName = "creation_db_test";

        dbManager.createDb(dbName);

        Integer foundedDb = (Integer) entityManager.createNativeQuery(
                " SELECT COUNT(*) FROM pg_database WHERE datname = '" + dbName + "'",
                        Integer.class)
                .getSingleResult();

        assertThat(foundedDb).isEqualTo(1);
    }


    @Test
    @SuppressWarnings("all")
    public void shouldCreateTemplateInDb()
    {
        String dbName = "db_to_creation_template_test";
        String tableName = "template_test";
        JdbcTemplate dynamicTemplate = createJdbcTemplateFor(dbName);

        SqlRowSet sqlRowSet = dynamicTemplate.query(
                "SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = '" + tableName + "') exist;",
                new SqlRowSetResultSetExtractor());
        closeConnection(dynamicTemplate);

        sqlRowSet.next();
        assertThat(sqlRowSet.getBoolean("exist")).isTrue();
    }


    @SuppressWarnings("all")
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
            @SuppressWarnings("all")
            Connection connection = dataSource.getConnection();
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
}

package com.barabanov.mandatory.model.dbms.db.repository;

import com.barabanov.mandatory.model.dbms.db.ContainerTestBase;
import com.barabanov.mandatory.model.dbms.dto.ColumnDesc;
import com.barabanov.mandatory.model.dbms.entity.SecurityLevel;
import com.barabanov.mandatory.model.dbms.repository.DbManager;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlRowSetResultSetExtractor;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;


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

    // TODO: 21.02.2024 узнать лучше делать тесты независимыми, но тогда повторно вызывать функционал или же делать их зависимыми,
    //  но не вызывать дополнительный функционал (думаю первое).
    @Test
    public void shouldDropDatabase()
    {
        String dbName = "drop_db_test";

        dbManager.createDb(dbName);
        dbManager.dropDb(dbName);

        Integer foundedDb = (Integer) entityManager.createNativeQuery(
                        " SELECT COUNT(*) FROM pg_database WHERE datname = '" + dbName + "'",
                        Integer.class)
                .getSingleResult();

        assertThat(foundedDb).isEqualTo(0);
    }

    
    @Test
    @SuppressWarnings("ConstantConditions")
    public void shouldCreateTemplateInDb()
    {
        String dbName = "db_to_creation_template_test";
        String tableName = "template_test";

        dbManager.createDb(dbName);
        dbManager.createTable(dbName, tableName, Collections.emptyList());

        JdbcTemplate dynamicTemplate = createJdbcTemplateFor(dbName);
        SqlRowSet sqlRowSet = dynamicTemplate.query(
                "SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = '" + tableName + "') exist;",
                new SqlRowSetResultSetExtractor());
        closeConnection(dynamicTemplate);

        sqlRowSet.next();
        assertThat(sqlRowSet.getBoolean("exist")).isTrue();
    }


    @Test
    @SuppressWarnings("ConstantConditions")
    public void shouldDropTemplateInDb()
    {
        String dbName = "db_to_drop_template_test";
        String tableName = "template_test";

        dbManager.createDb(dbName);
        dbManager.createTable(dbName, tableName, Collections.emptyList());
        dbManager.dropTable(dbName, tableName);

        JdbcTemplate dynamicTemplate = createJdbcTemplateFor(dbName);
        SqlRowSet sqlRowSet = dynamicTemplate.query(
                "SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = '" + tableName + "') exist;",
                new SqlRowSetResultSetExtractor());
        closeConnection(dynamicTemplate);

        assertThatNoException().isThrownBy(() -> sqlRowSet.next());
        assertThat(sqlRowSet.getBoolean("exist")).isFalse();
    }


    // Не проверяются ограничения этих столбцов потому что сложно :#
    @SuppressWarnings("ConstantConditions")
    @Test
    public void checkCreatedColumnsInTable()
    {
        String dbName = "db_to_check_template_test";
        String tableName = "car_template";
        Map<String, ColumnDesc> columnsDesc = Map.of(
                "id", new ColumnDesc("id", "BIGINT", List.of("PRIMARY KEY"), null),
                "model", new ColumnDesc("model", "CHARACTER", List.of("NOT NULL"), null),
                "price", new ColumnDesc("price", "INTEGER", Collections.emptyList(), SecurityLevel.OF_PARTICULAR_IMPORTANCE)
        );

        dbManager.createDb(dbName);
        dbManager.createTable(
                dbName,
                tableName,
                columnsDesc.values()
                        .stream()
                        .toList()
        );

        JdbcTemplate dynamicTemplate = createJdbcTemplateFor(dbName);
        SqlRowSet sqlRowSet = dynamicTemplate.query(
                "SELECT column_name, data_type FROM information_schema.columns WHERE table_name = '" + tableName + "';",
                new SqlRowSetResultSetExtractor());
        closeConnection(dynamicTemplate);

        while (sqlRowSet.next())
        {
            String extractedColName = sqlRowSet.getString("column_name");
            String extractedColType = sqlRowSet.getString("data_type");

            ColumnDesc columnDesc = columnsDesc.get(extractedColName);
            assertThat(columnDesc).isNotNull();
            assertThat(extractedColType.toUpperCase()).isEqualTo(columnDesc.getType());
        }
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


    @SuppressWarnings("ConstantConditions")
    private void closeConnection(JdbcTemplate template)
    {
        SingleConnectionDataSource dataSource = (SingleConnectionDataSource) template.getDataSource();

        dataSource.close();
    }
}

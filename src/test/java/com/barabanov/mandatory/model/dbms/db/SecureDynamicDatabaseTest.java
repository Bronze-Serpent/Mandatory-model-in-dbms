package com.barabanov.mandatory.model.dbms.db;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.*;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.*;
import com.barabanov.mandatory.model.dbms.secure.sql.dto.ColumnDesc;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface.DynamicDbService;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface.DynamicTableService;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface.DynamicTupleService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlRowSetResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.List;
import java.util.Optional;

import static com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel.*;
import static org.assertj.core.api.Assertions.assertThat;


@RequiredArgsConstructor
public class SecureDynamicDatabaseTest extends ContainerTestBase
{
    private final DynamicDbWorker dynamicDbWorker;
    private final DynamicDbService dynamicDbService;
    private final DynamicTableService dynamicTableService;
    private final DynamicTupleService secureDynamicTupleService;

    private final DbSecurityRepository dbSecurityRepository;
    private final TableSecurityRepository tableSecurityRepository;
    private final ColumnSecurityRepository columnSecurityRepository;
    private final TupleSecurityRepository tupleSecurityRepository;
    private final ValueSecurityRepository valueSecurityRepository;
    private final EntityManager entityManager;


    @SuppressWarnings("ConstantConditions")
    @Test
    public void shouldCreateDatabaseWithTablesAndData()
    {
        // �������� ���� ������
        String dbName = "test_db_1";
        dynamicDbService.createDb(dbName, SECRET);
        DatabaseSecurity db = dbSecurityRepository.findByName(dbName).get();

        // �������� �������� ��
        assertThat(db.getName()).isEqualTo(dbName);
        assertThat(db.getSecurityLevel()).isEqualTo(SECRET);
        Integer foundedDb = (Integer) entityManager.createNativeQuery(
                        " SELECT COUNT(*) FROM pg_database WHERE datname = '" + dbName + "'",
                        Integer.class)
                .getSingleResult();
        AssertionsForClassTypes.assertThat(foundedDb).isEqualTo(1);

        // �������� ������
        String tableName_1 = "car";
        List<ColumnDesc> columnsDesc_1 = List.of(
                new ColumnDesc("name", "VARCHAR(45)", List.of("UNIQUE"), TOP_SECRET),
                new ColumnDesc("id", "BIGSERIAL", List.of("PRIMARY KEY"), null)
        );
        dynamicTableService.createTableInDb(db.getId(), tableName_1, columnsDesc_1);

        String tableName_2 = "person";
        List<ColumnDesc> columnsDesc_2 = List.of(
                new ColumnDesc("name", "VARCHAR(45)", List.of("UNIQUE"), null),
                new ColumnDesc("id", "BIGSERIAL", List.of("PRIMARY KEY"), null)
        );
        dynamicTableService.createTableInDb(db.getId(), tableName_2, columnsDesc_2, TOP_SECRET);

        // �������� �������� ������
        TableSecurity tableSecurity_1 = tableSecurityRepository.findByNameInDb(db.getId(), tableName_1).get();
        assertThat(tableSecurity_1.getName()).isEqualTo(tableName_1);
        assertThat(tableSecurity_1.getSecurityLevel()).isEqualTo(SECRET);

        TableSecurity tableSecurity_2 = tableSecurityRepository.findByNameInDb(db.getId(), tableName_2).get();
        assertThat(tableSecurity_2.getName()).isEqualTo(tableName_2);
        assertThat(tableSecurity_2.getSecurityLevel()).isEqualTo(TOP_SECRET);

        JdbcTemplate dbJdbcTemplate = dynamicDbWorker.createJdbcTemplateFor(dbName);
        SqlRowSet tableRowSet = dbJdbcTemplate.query("SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = '" + tableName_1 + "') exist;",
                new SqlRowSetResultSetExtractor());
        tableRowSet.next();
        assertThat(tableRowSet.getBoolean(1)).isTrue();

        // �������� �������� �������
        ColumnSecurity column_1_1 = columnSecurityRepository.findByNameInTable(tableSecurity_1.getId(), "name").get();
        assertThat(column_1_1.getSecurityLevel()).isEqualTo(TOP_SECRET);
        ColumnSecurity column_1_2 = columnSecurityRepository.findByNameInTable(tableSecurity_1.getId(), "id").get();
        assertThat(column_1_2.getSecurityLevel()).isEqualTo(SECRET);

        SqlRowSet columnRowSet_1 = dbJdbcTemplate.query(
                "SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = 'public' AND table_name = '" + tableName_1 + "' AND column_name = 'name') exist;",
                new SqlRowSetResultSetExtractor());
        columnRowSet_1.next();
        SqlRowSet columnRowSet_2 = dbJdbcTemplate.query(
                "SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = 'public' AND table_name = '" + tableName_1 + "' AND column_name = 'id') exist;",
                new SqlRowSetResultSetExtractor());
        columnRowSet_2.next();
        assertThat(columnRowSet_1.getBoolean(1)).isTrue();
        assertThat(columnRowSet_2.getBoolean(1)).isTrue();

        // �������� ��������
        Long tuple_1_id = secureDynamicTupleService.insertIntoDb(db.getId(),
                """
                        INSERT INTO car (name)
                        VALUES ('VAZ' - OF_PARTICULAR_IMPORTANCE) -- TOP_SECRET;
                                """).getTupleId();

        Long tuple_2_id = secureDynamicTupleService.insertIntoDb(db.getId(),
                """
                        INSERT INTO person (name)
                        VALUES ('KALASHNIKOV' - OF_PARTICULAR_IMPORTANCE);
                                """).getTupleId();

        // �������� ��������� ��������
        assertThat(tuple_1_id).isNotNull();
        TupleSecurity tupleSecurity_1 = tupleSecurityRepository.findByTupleIdInTable(tableSecurity_1.getId(), tuple_1_id).get();
        assertThat(tupleSecurity_1.getSecurityLevel()).isEqualTo(TOP_SECRET);

        // �������� ��������� ��������
        ValueSecurity vaz_val_sec = valueSecurityRepository.findByTupleAndColumnId(tuple_1_id, column_1_1.getId()).get();
        assertThat(vaz_val_sec.getSecurityLevel()).isEqualTo(OF_PARTICULAR_IMPORTANCE);

        ColumnSecurity columnSecurity_2_1 = columnSecurityRepository.findByNameInTable(tableSecurity_2.getId(), "name").get();
        ValueSecurity kalashnikov_val_sec = valueSecurityRepository.findByTupleAndColumnId(tuple_2_id, columnSecurity_2_1.getId()).get();
        assertThat(kalashnikov_val_sec.getSecurityLevel()).isEqualTo(OF_PARTICULAR_IMPORTANCE);
        SqlRowSet carTuplesRowSet = dbJdbcTemplate.query("""
                        SELECT name
                        FROM car;""",
                new SqlRowSetResultSetExtractor());
        carTuplesRowSet.next();
        assertThat(carTuplesRowSet.getString(1)).isEqualTo("VAZ");

        dynamicDbWorker.closeConnection(dbJdbcTemplate);
    }


    @Test
    public void shouldDeleteDb()
    {
        String dbName = "test_creating_db_1";
        SecurityLevel securityLevel = TOP_SECRET;

        dynamicDbService.createDb(dbName, securityLevel);
        Optional<DatabaseSecurity> first_dbOptional = dbSecurityRepository.findByName(dbName);
        assertThat(first_dbOptional).isNotEmpty();
        DatabaseSecurity first_db = first_dbOptional.get();

        dynamicDbService.deleteDb(first_db.getId());
        Optional<DatabaseSecurity> second_db = dbSecurityRepository.findByName(dbName);
        assertThat(second_db).isEmpty();
    }
}

package com.barabanov.mandatory.model.dbms.db.repository;

import com.barabanov.mandatory.model.dbms.db.ContainerTestBase;
import com.barabanov.mandatory.model.dbms.repository.DbManager;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionOperations;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RequiredArgsConstructor
public class DbManagerTest extends ContainerTestBase
{
    private final DbManager dbManager;

    private final EntityManager entityManager;


    @Test
    public void shouldCreateDatabaseTest()
    {
        TransactionOperations.withoutTransaction().execute((TransactionCallback<Integer>) status -> null);
        String dbName = "creation_db_name_test";

        dbManager.createDb(dbName);

        Integer foundedDb = (Integer) entityManager.createNativeQuery(
                " SELECT COUNT(*) FROM pg_database WHERE datname = '" + dbName + "'",
                        Integer.class)
                .getSingleResult();

        assertThat(foundedDb).isEqualTo(1);
    }
}

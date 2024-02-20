package com.barabanov.mandatory.model.dbms.db;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;


@ActiveProfiles("test")
@SpringBootTest
public abstract class ContainerTestBase
{
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16.1");


    @BeforeAll
    static void runContainer()
    {
        container.start();
    }


    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername); // postgres by default
        registry.add("spring.datasource.password", container::getPassword); // postgres by default
    }
}

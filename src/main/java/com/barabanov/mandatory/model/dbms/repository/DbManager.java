package com.barabanov.mandatory.model.dbms.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class DbManager
{
    private final JdbcTemplate jdbcTemplate;


    public void createDb(String dbName)
    {
        jdbcTemplate.execute("CREATE DATABASE " + dbName);
    }


    public void dropDb(String dbName)
    {
        jdbcTemplate.execute("DROP DATABASE " + dbName);
    }
}

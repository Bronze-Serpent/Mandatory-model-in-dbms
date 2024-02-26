package com.barabanov.mandatory.model.dbms;

import com.barabanov.mandatory.model.dbms.db.DynamicDbWorker;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;


@TestConfiguration
@RequiredArgsConstructor
public class TestApplicationRunner
{
    private final Environment environment;


    @Bean
    public DynamicDbWorker dynamicDbWorker()
    {
        return new DynamicDbWorker(environment);
    }
}

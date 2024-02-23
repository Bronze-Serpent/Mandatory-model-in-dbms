package com.barabanov.mandatory.model.dbms.config;

import com.fasterxml.jackson.core.JsonFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class JsonMappingConfig
{

    @Bean
    public JsonFactory jsonFactory()
    {
        return new JsonFactory();
    }
}

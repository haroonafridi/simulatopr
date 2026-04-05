package com.hkcapital.portoflio.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig
{

    @Bean
    public ObjectMapper objectMapper() //
    {
        JsonFactory factory = JsonFactory.builder().build();
        ObjectMapper mapper = new ObjectMapper(factory);
        mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
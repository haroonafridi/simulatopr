package com.hkcapital.portflio.config;

import com.hkcapital.portflio.service.registry.Service;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "data")
@Data
public class DataPathConfig implements Service
{
    private String csv;
    private String json;

    public String getCsv()
    {
        return csv;
    }

    public String getJson()
    {
        return json;
    }
}

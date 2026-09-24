package com.hkcapital.portflio.config;

import com.hkcapital.portflio.service.registry.Service;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "simulation-config")
@Data
public class SimulationConfig implements Service
{
    private String dataFolder;
}

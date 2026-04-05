package com.hkcapital.portoflio.service.configuration;

import com.hkcapital.portoflio.model.Configuration;
import com.hkcapital.portoflio.service.registry.Service;

import java.util.List;

public interface ConfigurationService extends Service
{
    Configuration addConfiguration(Configuration configuration);

    void removeConfiguration(Configuration configuration);

    Configuration updateConfigration(Configuration configuration);

    void removeAll();

    Configuration findById(Integer id);

    List<Configuration> findAll();

    void removeById(Integer id);
}

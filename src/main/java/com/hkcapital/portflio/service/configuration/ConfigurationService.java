package com.hkcapital.portflio.service.configuration;

import com.hkcapital.portflio.model.Configuration;
import com.hkcapital.portflio.service.configuration.dto.ConfigurationDTO;
import com.hkcapital.portflio.service.registry.Service;

import java.util.List;

public interface ConfigurationService extends Service
{
    Configuration addConfiguration(Configuration configuration);

    void removeConfiguration(Configuration configuration);

    Configuration updateConfiguration(Configuration configuration);

    void removeAll();

    Configuration findById(Integer id);

    List<Configuration> findAll();

    void removeById(Integer id);

    Configuration findByConfiguration(ConfigurationDTO configurationDTO);

    Configuration findByCode(String code);
}

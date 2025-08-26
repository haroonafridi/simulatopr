package com.hkcapital.portoflio.service.impl;

import com.hkcapital.portoflio.repository.ConfigurationRepository;
import com.hkcapital.portoflio.service.ConfigurationService;
import org.springframework.stereotype.Service;
import com.hkcapital.portoflio.model.Configuration;

@Service
public class ConfigurationServiceImpl implements ConfigurationService
{
    private final ConfigurationRepository configurationRepository;

    public ConfigurationServiceImpl(ConfigurationRepository configurationRepository)
    {
        this.configurationRepository = configurationRepository;
    }

    @Override
    public Configuration addConfiguration(Configuration configuration)
    {
        return configurationRepository.save(configuration);
    }

    @Override
    public void removeConfiguration(Configuration configuration)
    {
        configurationRepository.delete(configuration);
    }

    @Override
    public Configuration updateConfigration(Configuration configuration)
    {
        return configurationRepository.save(configuration);
    }
}

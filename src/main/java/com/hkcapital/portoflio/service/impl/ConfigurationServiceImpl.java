package com.hkcapital.portoflio.service.impl;

import com.hkcapital.portoflio.model.Configuration;
import com.hkcapital.portoflio.repository.ConfigurationRepository;
import com.hkcapital.portoflio.service.ConfigurationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public void removeAll()
    {
        configurationRepository.deleteAll();
    }

    @Override
    public Configuration findById(Integer id)
    {
        Optional<Configuration> configuration = configurationRepository.findById(id);
        return !configuration.isEmpty() ? configuration.get() : null;
    }


    @Override
    public List<Configuration> findAll()
    {
        return configurationRepository.findAll();
    }

    @Override
    public void removeById(Integer id)
    {
        configurationRepository.findById(id) //
                .ifPresent(configuration -> configurationRepository.delete(configuration));
    }
}

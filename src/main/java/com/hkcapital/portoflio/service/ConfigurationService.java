package com.hkcapital.portoflio.service;

import com.hkcapital.portoflio.model.Configuration;

import java.util.List;

public interface ConfigurationService
{
    Configuration addConfiguration(Configuration configuration);
     void removeConfiguration(Configuration configuration);

    Configuration updateConfigration(Configuration configuration);

    void removeAll();

    Configuration findById(Integer id);

    List<Configuration> findAll();

    void removeById(Integer id);
}

package com.hkcapital.portoflio.service;

import com.hkcapital.portoflio.model.Configuration;

public interface ConfigurationService
{
    Configuration addConfiguration(Configuration configuration);
     void removeConfiguration(Configuration configuration);

    Configuration updateConfigration(Configuration configuration);
}

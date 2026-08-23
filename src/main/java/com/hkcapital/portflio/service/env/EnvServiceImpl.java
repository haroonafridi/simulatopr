package com.hkcapital.portflio.service.env;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class EnvServiceImpl implements EnvService
{
    private final Environment environment;

    public EnvServiceImpl(Environment environment)
    {
        this.environment = environment;
    }

    public String getActiveProfile()
    {
        return Arrays.stream(environment.getActiveProfiles())
                .findFirst()
                .orElse("default");
    }
}
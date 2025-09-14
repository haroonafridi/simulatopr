package com.hkcapital.portoflio.service.impl;

import com.hkcapital.portoflio.repository.ServiceRegistery;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ServiceRegistryImpl<T> implements ServiceRegistery<T>
{
    private final Map<String, T> serviceRegistry = new HashMap();

    @Override
    public T getService(final String serviceName)
    {
        return serviceRegistry.get(serviceName);
    }

    @Override
    public void putService(String serviceName, T service)
    {
        serviceRegistry.put(serviceName, service);
    }

}

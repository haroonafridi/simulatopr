package com.hkcapital.portoflio.repository.registry;

public interface ServiceRegistery<T>
{
    T getService(final String serviceName);

    void putService(final String serviceName, T service);
}

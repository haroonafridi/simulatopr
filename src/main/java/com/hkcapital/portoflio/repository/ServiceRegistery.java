package com.hkcapital.portoflio.repository;

public interface ServiceRegistery<T>
{
    T getService(final String serviceName);

    void putService(final String serviceName, T service);
}

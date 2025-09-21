package com.hkcapital.portoflio.model;

public enum Keys
{
    CONFIGURATION_ID("configuration.id");

    private  String key;
    Keys(String key)
    {
        this.key = key;
    }

    public String getKey()
    {
        return key;
    }
}

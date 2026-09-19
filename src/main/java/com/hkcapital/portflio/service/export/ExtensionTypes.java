package com.hkcapital.portflio.service.export;

public enum ExtensionTypes
{
    csv(".csv"),
    json(".json");

    private final String type;

    ExtensionTypes(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
}

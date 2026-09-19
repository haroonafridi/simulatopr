package com.hkcapital.portflio.service.export;

public enum FileTypes
{
    TICK("tick"),
    CANDLE("candle"),
    BAND("band");

    private final String type;

    FileTypes(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
}

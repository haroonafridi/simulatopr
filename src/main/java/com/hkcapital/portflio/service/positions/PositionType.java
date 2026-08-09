package com.hkcapital.portflio.service.positions;

public enum PositionType
{
    BUY("BUY"),
    SELL("SELL"),
    BOTH("BOTH");

    private String value;

    PositionType(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}

package com.hkcapital.portflio.market.structure;

public enum BandType
{
    HIGH("high"),
    LOW("low");

    private String value;

    BandType(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}

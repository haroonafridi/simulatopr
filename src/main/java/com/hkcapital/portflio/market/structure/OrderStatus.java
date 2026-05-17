package com.hkcapital.portflio.market.structure;

public enum OrderStatus
{
    OPEN("open"),
    CLOSE("close");

    private String value;

    OrderStatus(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}

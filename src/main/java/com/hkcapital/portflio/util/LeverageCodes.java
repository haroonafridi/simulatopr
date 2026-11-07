package com.hkcapital.portflio.util;

public enum LeverageCodes
{
    Lev_20_15Per ("Lev_20_15Per");

    private String value;

    LeverageCodes(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}

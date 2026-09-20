package com.hkcapital.portflio.service.importexport;

public enum Literals
{
    UNDER_SCORE("_"),
    DASH("-");

    private String value;

    Literals(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}

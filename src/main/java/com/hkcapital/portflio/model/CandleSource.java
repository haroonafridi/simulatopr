package com.hkcapital.portflio.model;

public enum CandleSource
{
    ETORO("etoro"),
    INTERNAL("internal"),
    ;
    private String source;

    CandleSource(String source)
    {
        this.source = source;
    }

    public String getSource()
    {
        return source;
    }
}

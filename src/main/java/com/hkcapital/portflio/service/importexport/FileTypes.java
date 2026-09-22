package com.hkcapital.portflio.service.importexport;

public enum FileTypes
{
    TICK("tick"),
    CANDLE("candle"),
    BAND("band"),
    MARKET_STRUCTURE("market-structure"),
    SR_MATRIX("sr-matrix"),
    SR_TOLERANCE("sr-tolerance"),

    MARKET_STRUCTURE_CONF("market_structure_conf"),

    STRATEGY("strategy");
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

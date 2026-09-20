package com.hkcapital.portflio.service.importexport;

public enum FileTypes
{
    TICK("tick"),
    CANDLE("candle"),
    BAND("band"),
    MARKET_STRUCTURE("market-structure"),
    SR_MATRIX("sr-matrix"),
    SR_MATRIX_TOLERANCE("sr-matrix-tolerance"),

    MARKET_STRUCTURE_CONF("market_structure_conf");
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

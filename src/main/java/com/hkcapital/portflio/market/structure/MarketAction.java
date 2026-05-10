package com.hkcapital.portflio.market.structure;

public enum MarketAction
{
    BUY("buy"),
    SELL("sell"),
    NONE("none");

    private String value;

    MarketAction(String value)
    {
        this.value = value;
    }
}

package com.hkcapital.portflio.market.structure;

public enum MarketRegime
{
    TRENDING_UP("trending_up"),
    TRENDING_DOWN("trending_down"),

    CHOP("chop");

    private String value;

    MarketRegime(String value)
    {
        this.value = value;
    }
}

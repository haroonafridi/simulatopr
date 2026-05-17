package com.hkcapital.portflio.market.structure;

public enum MarketTypes
{
    GOLD_15_MIN("gold_15_mins"),
    GOLD_30_MIN("gold_30_mins"),
    GOLD_1_HOUR("gold_1_hour"),
    GOLD_4_HOUR("gold_4_hour"),
    GOLD_1_DAY("gold_1_day"),
    GOLD_1_WEEK("gold_1_week");

    private String value;

    MarketTypes(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}

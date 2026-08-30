package com.hkcapital.portflio.market.structure;

public enum MarketTypes
{
    GOLD_1_MIN("gold_1_mins"),
    GOLD_5_MIN("gold_5_mins"),
    GOLD_15_MIN("gold_15_mins"),
    GOLD_30_MIN("gold_30_mins"),
    GOLD_1_HOUR("gold_1_hour"),
    GOLD_4_HOUR("gold_4_hour"),
    // GOLD_1_DAY("gold_1_day"),
    //GOLD_1_WEEK("gold_1_week"),

    NASDAQ_1_MIN("nasdaq_1_mins"),
    NASDAQ_5_MIN("nasdaq_5_mins"),
    NASDAQ_15_MIN("nasdaq_15_mins"),
    NASDAQ_30_MIN("nasdaq_30_mins"),
    NASDAQ_1_HOUR("nasdaq_1_hour"),
    NASDAQ_4_HOUR("nasdaq_4_hour"),
    // NASDAQ_1_DAY("nasdaq_1_day"),
    // NASDAQ_1_WEEK("nasdaq_1_week");


    BTC_1_MIN("btc_1_mins"),
    BTC_5_MIN("btc_5_mins"),
    BTC_15_MIN("btc_15_mins"),
    BTC_30_MIN("btc_30_mins"),
    BTC_1_HOUR("btc_1_hour"),
    BTC_4_HOUR("btc_4_hour");


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

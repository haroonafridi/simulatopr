package com.hkcapital.portflio.ui.panels.instrument.labels;

public enum Labels
{
    Id("Id"),
    Instrument("Instrument"),

    InstrumentConfiguration("Instrument Configuration"),
    Ticker("Ticker"),
    Name("Name"),
    MaxSlippage("Max Slippage"),
    EtoroInstrumentId("Etoro Instrument Id"),
    Active("Active"),
    WithCandle("With Candle"),
    WithFeed("With Feed"),
    WithBands("With Bands"),

    WithDynamicOrder("With Dynamic Order"),
    Url("Url"),
    InstrumentPanel("Instrument Panel"),


    TimeFrame("Time Frame"),
    TimeFrameUnit("Time Frame Unit"),
    Module("Module"),
    Sub("Sub"),
    Interval("Interval"),

    StructureName("Structure Name"),

    TrendStructureName("Trend Structure Name"),

    OrderBlockDepth("Order Block Depth"),

    Order("Order"),

    RsiOverSold("RSI Oversold"),
    RsiOverBought("RSI Overbought"),
    RsiBearishFrom("RSI Bearish From"),
    RsiBearishTo("RSI Bearish To"),
    RsiBullishFrom("RSI Bullish From"),
    RsiBullishTo("RSI Bullish TO"),
    Atr("ATR");


    private String label;

    Labels(String label)
    {
        this.label = label;
    }

    public String getLabel()
    {
        return label;
    }
}

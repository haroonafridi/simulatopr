package com.hkcapital.portflio.ui.panels.instrument.labels;

public enum Labels
{
    Id("Id:"),
    Instrument("Instrument:"),

    InstrumentConfiguration("Instrument Configuration:"),
    Ticker("Ticker:"),
    Name("Name:"),
    MaxSlippage("Max Slippage:"),
    EtoroInstrumentId("Etoro Instrument Id:"),
    Active("Active:"),
    WithCandle("With Candle:"),
    WithFeed("With Feed:"),
    WithBands("With Bands:"),
    Url("Url:"),
    InstrumentPanel("Instrument Panel:"),


    TimeFrame("Time Frame:"),
    TimeFrameUnit("Time Frame Unit:"),
    Module("Module"),
    Sub("Sub"),
    Interval("Interval"),

    StructureName("Structure Name"),

    Order("Order");

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

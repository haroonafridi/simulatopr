package com.hkcapital.portoflio.ui.panels.instrument.labels;

public enum Labels
{
    Id("Id:"),
    Instrument("Instrument:"),
    Ticker("Ticker:"),
    Name("Name:"),
    MaxSlippage("Max Slippage:"),
    EtoroInstrumentId("Etoro Instrument Id:"),
    Active("Active:"),

    Url("Url:"),
    InstrumentPanel("Instrument Panel:");

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

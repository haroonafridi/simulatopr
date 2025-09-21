package com.hkcapital.portoflio.ui.panels.marketconditions.labels;

public enum Labels
{
    Id("Id"),
    MarketConditions("Market conditions"),
    InstrumentName("Instrument Name"),
    DayLow("Day low"),
    DayHigh("Day High"),

   PercentMove("Percent Move"),
    InstrumentPanel("Instrument Panel");


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

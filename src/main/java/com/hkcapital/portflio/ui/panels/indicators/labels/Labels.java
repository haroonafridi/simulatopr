package com.hkcapital.portflio.ui.panels.indicators.labels;

public enum Labels
{
    Id("Id:"),
    Name("Name"),
    Instrument("Instrument:"),
    Indicators("Indicators:"),
    Period("Period"),
    TimeFrame("TimeFrame"),
    TimeFrameUnit("TimeFrame Unit");

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

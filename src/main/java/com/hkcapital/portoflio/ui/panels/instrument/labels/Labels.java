package com.hkcapital.portoflio.ui.panels.instrument.labels;

public enum Labels
{
    Id("Id"),
    Instrument("Instrument"),
    Name("Name"),
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

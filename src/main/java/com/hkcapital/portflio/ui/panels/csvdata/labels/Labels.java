package com.hkcapital.portflio.ui.panels.csvdata.labels;

public enum Labels
{
    CSVData("CSV Data"),
    Generate("Generate"),
    CandleData("Candle Data"),
    TickData("Tick Data");

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

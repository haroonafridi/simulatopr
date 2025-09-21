package com.hkcapital.portoflio.ui.panels.configuartion.labels;

public enum Labels
{
    Id("Id"),
    Configuration("Configuration"),
    PercentAllowedAllocation("% Allocation allowed"),
    NoOfInstruments("No of Instruments"),
    NoofPositionsPerinstruments(" No of Positions per instruments"),
    MaxPercentAllowedPerPosition("Max % percent allowed per instrument"),
    Lev("Lev");

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

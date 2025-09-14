package com.hkcapital.portoflio.ui.panels.capital;

public enum Labels
{
    Date("Date"),
    OpeningCapital("Opening capital:"),

    AllocatedCapital("Allocated capital:"),

    Capital("Capital");


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

package com.hkcapital.portoflio.ui.panels.configuartion;

import java.util.Arrays;
import java.util.stream.Collectors;

enum ConfigurationTableHeader
{
    Id("Id"),
    PercentAllowedAllocation("% Allocation allowed"),
    NoOfInstruments("No of Instruments"),
    NoofPositionsPerinstruments(" No of Positions per instruments"),
    MaxPercentAllowedPerPosition("Max % percent allowed per instrument"),
    Lev("Lev");

    private final String value;

    ConfigurationTableHeader(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }

    public static  String[] labels()
    {
        return Arrays.stream(ConfigurationTableHeader.values()).map(v -> v.getValue()).collect(Collectors.toList()).toArray(new String[0]);
    }
}

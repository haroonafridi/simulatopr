package com.hkcapital.portflio.market.indicators;

public enum TimeFramesUnit
{
    SECOND("SECOND"),
    MINUTE("MINUTE"),
    HOUR("HOUR"),
    DAY("DAY"),
    WEEK("WEEK");
    private String unit;

    TimeFramesUnit(String unit)
    {
        this.unit = unit;
    }

    public String getUnit()
    {
        return unit;
    }
}

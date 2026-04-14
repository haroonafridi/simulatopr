package com.hkcapital.portoflio.indicators;

public enum Unit
{
    MINUTE("m"),
    HOUR("hour"),
    DAY("day"),
    WEEK("week");
    private String unit;

    Unit(String unit)
    {
        this.unit = unit;
    }

    public String getUnit()
    {
        return unit;
    }
}

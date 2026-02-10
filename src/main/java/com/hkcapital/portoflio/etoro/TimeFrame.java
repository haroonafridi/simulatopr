package com.hkcapital.portoflio.etoro;

public enum TimeFrame
{
    OneMinute("OneMinute"),
    FiveMinutes("FiveMinutes"),
    TenMinutes("TenMinutes"),
    FifteenMinutes("FifteenMinutes"),
    ThirtyMinutes("ThirtyMinutes"),
    OneHour("OneHour"),
    FourHours("FourHours"),
    OneDay("OneDay"),
    OneWeek("OneWeek");
    private String timeFrame;
    TimeFrame(String timeFrame)
    {
        this.timeFrame = timeFrame;
    }

    public String getTimeFrame()
    {
        return timeFrame;
    }
}

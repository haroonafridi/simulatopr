package com.hkcapital.portoflio.ui.panels.tradingsessions;

import java.util.Arrays;
import java.util.stream.Collectors;

enum TradingSessionTableHeader
{
    Id("Id"),
    name("Name"),
    description("Description"),

    startTime("Start Time"),
    endTime("End Time");

    private final String value;

    TradingSessionTableHeader(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }

    public static  String[] labels()
    {
        return Arrays.stream(TradingSessionTableHeader.values()).map(v -> v.getValue()).collect(Collectors.toList()).toArray(new String[0]);
    }
}

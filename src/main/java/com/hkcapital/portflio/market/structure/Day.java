package com.hkcapital.portflio.market.structure;

import lombok.Getter;

public enum Day
{
    SUNDAY("sunday"),
    MONDAY("monday"),
    TUESDAY("tuesday"),
    WEDENSDAY("wedensday"),
    THURSDAY("thursday"),
    FRIDAY("friday"),
    SATURDAY("saturday");
    @Getter
    private final String value;

    Day(String value)
    {
        this.value = value;
    }
}

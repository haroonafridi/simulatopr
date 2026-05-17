package com.hkcapital.portflio.market.structure;

import lombok.Getter;

public enum OrderType
{
    BUY("buy"),
    SELL("sell");
    @Getter
    private final String value;

    OrderType(String value)
    {
        this.value = value;
    }


}

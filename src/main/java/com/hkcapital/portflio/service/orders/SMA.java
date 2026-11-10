package com.hkcapital.portflio.service.orders;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SMA
{
    private Double sma9;
    private Double sma20;
    private Double sma50;
    private Double sma100;

    public static SMA Of(Double sma9,
                         Double sma20,
                         Double sma50,
                         Double sma100)
    {
        return new SMA(
                sma9,
                sma20,
                sma50,
                sma100
        );
    }
}

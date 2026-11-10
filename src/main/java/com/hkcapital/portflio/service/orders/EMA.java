package com.hkcapital.portflio.service.orders;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EMA
{
    private Double ema9;
    private Double ema20;
    private Double ema50;
    private Double ema100;

    public static EMA Of(Double ema9,
                         Double ema20,
                         Double ema50,
                         Double ema100)
    {
        return new EMA(ema9, ema20, ema50, ema100);
    }
}

package com.hkcapital.portflio.service.candle.etoro.impl;

import com.hkcapital.portflio.market.indicators.CandleBuilder;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public class SignalBuilder
{
    private List<CandleBuilder> candleBuilder;

    public List<CandleBuilder> getCandleBuilder()
    {
        if (candleBuilder == null)
        {
            return new ArrayList<>();
        }

        return candleBuilder;
    }
}

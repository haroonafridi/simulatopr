package com.hkcapital.portflio.service.candle.etoro.impl;

import com.hkcapital.portflio.market.indicators.CandleBuilder;
import com.hkcapital.portflio.model.Instrument;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public class SignalBuilder
{
    private List<CandleBuilder> candleBuilder;

    List<Instrument> instruments;

    public List<CandleBuilder> getCandleBuilder()
    {
        if (candleBuilder == null)
        {
            candleBuilder = new ArrayList<>();

            return candleBuilder;
        }
        return candleBuilder;
    }

    public List<Instrument> getInstruments()
    {
        if (instruments == null)
        {
            instruments = new ArrayList<>();
            return instruments;
        }
        return instruments;
    }
}

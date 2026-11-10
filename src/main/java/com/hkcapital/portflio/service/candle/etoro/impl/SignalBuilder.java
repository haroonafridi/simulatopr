package com.hkcapital.portflio.service.candle.etoro.impl;

import com.hkcapital.portflio.market.indicators.CandleBuilder;
import com.hkcapital.portflio.model.Instrument;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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


    public CandleBuilder getCandlesBuilderByInstrumentAndByTimeFrameAndTimeUnit(Instrument instrument,
                                                                                Integer timeFrame,
                                                                                String timeFrameUnit)
    {
        return getCandleBuilder()
                .stream()
                .filter(e -> e.getInterval().intValue() == timeFrame
                        && e.getTimeFrame().getUnit().equals(timeFrameUnit))
                .collect(Collectors.toList()).stream()
                .filter(cb -> cb.getInstrument()
                        .getInstrumentTicker()
                        .equals(instrument.getInstrumentTicker()))
                .findFirst().get();
    }


}

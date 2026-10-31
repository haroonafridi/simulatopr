package com.hkcapital.portflio.service.orders;

import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portflio.service.candle.etoro.impl.SignalBuilder;

import java.util.List;

public class DynamicOrderManager implements OrderProcessor
{

    private final List<IndicatorProcessor> indicators;

    public DynamicOrderManager(List<IndicatorProcessor> indicators)
    {
        this.indicators = indicators;
    }

    @Override
    public void process(LiveInstrumentRate liveInstrumentRate, SignalBuilder signalBuilder)
    {

    }
}

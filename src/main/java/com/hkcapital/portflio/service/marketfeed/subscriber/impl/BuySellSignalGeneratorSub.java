package com.hkcapital.portflio.service.marketfeed.subscriber.impl;

import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portflio.service.candle.etoro.impl.SignalBuilder;
import com.hkcapital.portflio.service.marketfeed.subscriber.MarketFeedSubscriber;
import org.springframework.stereotype.Service;

@Service
public class BuySellSignalGeneratorSub implements MarketFeedSubscriber
{
    @Override
    public void process(LiveInstrumentRate liveInstrumentRate , SignalBuilder signalBuilder)
    {

    }
}

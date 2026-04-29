package com.hkcapital.portoflio.service.marketfeed.subscriber.impl;

import com.hkcapital.portoflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portoflio.service.candle.etoro.impl.SignalBuilder;
import com.hkcapital.portoflio.service.marketfeed.subscriber.MarketFeedSubscriber;
import org.springframework.stereotype.Service;

@Service
public class BuySellSignalGeneratorSub implements MarketFeedSubscriber
{
    @Override
    public void process(LiveInstrumentRate liveInstrumentRate , SignalBuilder signalBuilder)
    {

    }
}

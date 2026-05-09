package com.hkcapital.portflio.service.marketfeed.subscriber;

import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portflio.service.candle.etoro.impl.SignalBuilder;

public interface MarketFeedSubscriber
{
    void process(LiveInstrumentRate liveInstrumentRate, SignalBuilder signalBuilder);
}

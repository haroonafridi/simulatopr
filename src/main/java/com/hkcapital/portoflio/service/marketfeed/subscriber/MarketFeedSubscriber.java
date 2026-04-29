package com.hkcapital.portoflio.service.marketfeed.subscriber;

import com.hkcapital.portoflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portoflio.service.candle.etoro.impl.SignalBuilder;

public interface MarketFeedSubscriber
{
    void process(LiveInstrumentRate liveInstrumentRate, SignalBuilder signalBuilder);
}

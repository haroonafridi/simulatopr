package com.hkcapital.portoflio.service.marketfeed.observer;

import com.hkcapital.portoflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portoflio.service.candle.etoro.impl.SignalBuilder;
import com.hkcapital.portoflio.service.marketfeed.subscriber.MarketFeedSubscriber;

public interface MarketFeedObserver
{
    void process(LiveInstrumentRate liveInstrumentRate, SignalBuilder signalBuilder);
    void addMarketFeedSubscriber(MarketFeedSubscriber marketFeedSubscriber);

}

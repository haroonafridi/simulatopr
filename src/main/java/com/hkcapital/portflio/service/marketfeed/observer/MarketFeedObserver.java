package com.hkcapital.portflio.service.marketfeed.observer;

import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portflio.service.candle.etoro.impl.SignalBuilder;
import com.hkcapital.portflio.service.marketfeed.subscriber.MarketFeedSubscriber;

public interface MarketFeedObserver
{
    void process(LiveInstrumentRate liveInstrumentRate, SignalBuilder signalBuilder);
    void addMarketFeedSubscriber(MarketFeedSubscriber marketFeedSubscriber);

}

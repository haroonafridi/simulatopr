package com.hkcapital.portoflio.service;

import com.hkcapital.portoflio.etoro.websocket.LiveInstrumentRate;

public interface MarketFeedObserver
{
    void process(LiveInstrumentRate liveInstrumentRate);
    void addMarketFeedSubscriber(MarketFeedSubscriber marketFeedSubscriber);

}

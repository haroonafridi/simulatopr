package com.hkcapital.portoflio.service.marketfeed.subscriber;

import com.hkcapital.portoflio.service.api.etoro.websocket.LiveInstrumentRate;

public interface MarketFeedSubscriber
{
    void process(LiveInstrumentRate liveInstrumentRate);
}

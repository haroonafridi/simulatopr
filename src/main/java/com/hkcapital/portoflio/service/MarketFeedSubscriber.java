package com.hkcapital.portoflio.service;

import com.hkcapital.portoflio.etoro.websocket.LiveInstrumentRate;

public interface MarketFeedSubscriber
{
    void process(LiveInstrumentRate liveInstrumentRate);
}

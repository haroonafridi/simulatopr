package com.hkcapital.portoflio.service.marketfeed.observer.impl;

import com.hkcapital.portoflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portoflio.service.candle.etoro.impl.SignalBuilder;
import com.hkcapital.portoflio.service.marketfeed.observer.MarketFeedObserver;
import com.hkcapital.portoflio.service.marketfeed.subscriber.MarketFeedSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MarketFeedObserverImpl implements MarketFeedObserver
{
    private final Logger logger = LoggerFactory.getLogger(MarketFeedObserverImpl.class);
    private final List<MarketFeedSubscriber> feedSubscribers = new ArrayList<>();

    @Override
    public void process(LiveInstrumentRate liveInstrumentRate, SignalBuilder signalBuilder)
    {
        if (liveInstrumentRate != null)
        {
            feedSubscribers.forEach(feedSubscriber -> feedSubscriber.process(liveInstrumentRate, signalBuilder));
        }
    }

    @Override
    public void addMarketFeedSubscriber(MarketFeedSubscriber marketFeedSubscriber)
    {
        feedSubscribers.add(marketFeedSubscriber);
    }

}

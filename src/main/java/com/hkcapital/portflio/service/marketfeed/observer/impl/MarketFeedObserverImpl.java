package com.hkcapital.portflio.service.marketfeed.observer.impl;

import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portflio.service.candle.etoro.impl.SignalBuilder;
import com.hkcapital.portflio.service.marketfeed.observer.MarketFeedObserver;
import com.hkcapital.portflio.service.marketfeed.subscriber.MarketFeedSubscriber;
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

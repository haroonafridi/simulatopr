package com.hkcapital.portoflio.service.impl;

import com.hkcapital.portoflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portoflio.service.candle.etoro.impl.SignalBuilder;
import com.hkcapital.portoflio.service.marketfeed.observer.MarketFeedObserver;
import com.hkcapital.portoflio.service.marketfeed.observer.impl.MarketFeedObserverImpl;
import com.hkcapital.portoflio.service.marketfeed.subscriber.MarketFeedSubscriber;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.List;

class MarketFeedProcessorImplUnitTest
{
    @Test
    void shouldVerifyMarketFeedObserverProcess()
    {
        LiveInstrumentRate liveInstrumentRate = getLiveInstrumentRate();
        MarketFeedObserver marketFeedObserver = Mockito.mock(MarketFeedObserverImpl.class);
        marketFeedObserver.process(liveInstrumentRate, SignalBuilder.builder().build());
        Mockito.verify(marketFeedObserver).process(liveInstrumentRate, SignalBuilder.builder().build());
    }

    @Test
    void shouldVerifyMarketFeedObserverAddSubscriber()
    {
        MarketFeedSubscriber marketFeedSubscriber = Mockito.mock(MarketFeedSubscriber.class);
        MarketFeedObserver marketFeedObserver = Mockito.mock(MarketFeedObserver.class);
        marketFeedObserver.addMarketFeedSubscriber(marketFeedSubscriber);
        Mockito.verify(marketFeedObserver).addMarketFeedSubscriber(marketFeedSubscriber);
    }

    @Test
    void shouldVerifyAddingMultipleMarketFeedObserver()
    {
        MarketFeedSubscriber s0 = Mockito.mock(MarketFeedSubscriber.class);
        MarketFeedSubscriber s1 = Mockito.mock(MarketFeedSubscriber.class);
        MarketFeedSubscriber s2 = Mockito.mock(MarketFeedSubscriber.class);
        MarketFeedObserver marketFeedObserver = Mockito.mock(MarketFeedObserver.class);
        marketFeedObserver.addMarketFeedSubscriber(s0);
        marketFeedObserver.addMarketFeedSubscriber(s1);
        marketFeedObserver.addMarketFeedSubscriber(s2);
        Mockito.verify(marketFeedObserver).addMarketFeedSubscriber(s0);
        Mockito.verify(marketFeedObserver).addMarketFeedSubscriber(s1);
        Mockito.verify(marketFeedObserver).addMarketFeedSubscriber(s2);
        Mockito.verify(marketFeedObserver, Mockito.times(3)) //
                .addMarketFeedSubscriber(Mockito.any());
    }

    private static LiveInstrumentRate getLiveInstrumentRate()
    {
        LiveInstrumentRate liveInstrumentRate = LiveInstrumentRate.builder().instrumentId(18)//
                .ask(5000d).bid(5001d).allowBuy(true).allowSell(false).askDiscounted(0d)//
                .availabilityReason(0).bidDiscounted(0d).conversionRateAsk(1d).date(Instant.now())//
                .conversionRateBid(1d).isInstrumentActive(true).delayedAsk(1).isMarketOpen(true)//
                .priceRateId(1l).delayedBid(4500).isExchangeOpen(true).isOfficialClosingPrice(true)//
                .lastExecution(5000d).maxPositionUnits(1).newUnitMargin(1d)//
                .officialClosingPrice(5000d).unitMarginAsk(4500d).unitMarginBid(4500d).unitMarginAskDiscounted(4500d)//
                .unitMarginBidDiscounted(4500d).build();
        return liveInstrumentRate;
    }

    @Test
    void shouldVerifyInvocationOfProcessOnSubscriber()
    {
        List<MarketFeedSubscriber> subscribers = List.of(Mockito.mock(MarketFeedSubscriber.class), //
                Mockito.mock(MarketFeedSubscriber.class), //
                Mockito.mock(MarketFeedSubscriber.class));
        MarketFeedObserver marketFeedObserver = new MarketFeedObserverImpl();
        subscribers.forEach(subscriber -> marketFeedObserver.addMarketFeedSubscriber(subscriber));
        LiveInstrumentRate rate = getLiveInstrumentRate();
        marketFeedObserver.process(rate, SignalBuilder.builder().build());
        subscribers.forEach(s -> Mockito.verify(s).process(rate, SignalBuilder.builder().build()));
    }
}
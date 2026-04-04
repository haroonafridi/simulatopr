package com.hkcapital.portoflio.service.impl;

import com.hkcapital.portoflio.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portoflio.repository.LiveInstrumentFeedRepository;
import com.hkcapital.portoflio.service.MarketFeedObserver;
import com.hkcapital.portoflio.service.MarketFeedSubscriber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@SpringBootTest
class MarketFeedProcessorLiveMarketFeedDbWriterITTest
{
    @Autowired
    private MarketFeedObserver marketFeedObserver;
    @Autowired
    private MarketFeedDbWriter marketFeedDbWriter;
    @Autowired
    private LiveInstrumentFeedRepository feedRepository;
    @Test
    public void shouldSubscribeToLiveFeed()
    {
        marketFeedObserver.addMarketFeedSubscriber(marketFeedDbWriter);
        marketFeedObserver.process(getLiveInstrumentRate());
        System.out.println("size = "+feedRepository.findAll().size());
        Assertions.assertTrue(feedRepository.findAll().size() > 1) ;
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
}
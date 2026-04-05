package com.hkcapital.portoflio.service.impl;

import com.hkcapital.portoflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portoflio.model.LiveInstrumentFeed;
import com.hkcapital.portoflio.service.instrument.LiveInstrumentFeedDbService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

@SpringBootTest
class LiveInstrumentFeedDbServiceImplTest
{
    private static final Instant FEED_DATE =
            Instant.parse("2026-01-01T10:00:00Z");
    @Autowired
    private LiveInstrumentFeedDbService<LiveInstrumentRate> feedDbService;

    @Test
    public void shouldCreateLiveFeedInDb()
    {
        final LiveInstrumentFeed feed = feedDbService.save(getLiveInstrumentRate());
        Assertions.assertTrue(feed.getId() > 0);
        Assertions.assertEquals(feed.getInstrumentId(), 18);
        Assertions.assertEquals(feed.getAsk(), 5000d);
        Assertions.assertEquals(feed.getBid(), 5001d);
        Assertions.assertTrue(feed.getAllowBuy());
        Assertions.assertFalse(feed.getAllowSell());
        Assertions.assertEquals(feed.getAskDiscounted(), 0d);
        Assertions.assertEquals(feed.getAvailabilityReason(), 0);
        Assertions.assertEquals(feed.getBidDiscounted(), 0);
        Assertions.assertEquals(feed.getConversionRateAsk(), 1);
        Assertions.assertEquals(feed.getConversionRateBid(), 1d);
        Assertions.assertTrue(feed.getIsInstrumentActive());
        Assertions.assertEquals(feed.getDelayedAsk(), 1);
        Assertions.assertTrue(feed.getIsMarketOpen());
        Assertions.assertEquals(feed.getPriceRateId(), 1L);
        Assertions.assertEquals(feed.getDelayedBid(), 4500);
        Assertions.assertTrue(feed.getIsExchangeOpen());
        Assertions.assertTrue(feed.getIsOfficialClosingPrice());
        Assertions.assertEquals(feed.getLastExecution(), 5000d);
        Assertions.assertEquals(feed.getMaxPositionUnits(), 1);
        Assertions.assertEquals(feed.getNewUnitMargin(), 1);
        Assertions.assertEquals(feed.getOfficialClosingPrice(), 5000d);
        Assertions.assertEquals(feed.getUnitMarginAsk(), 4500d);
        Assertions.assertEquals(feed.getUnitMarginBid(), 4500d);
        Assertions.assertEquals(feed.getUnitMarginAskDiscounted(), 4500d);
        Assertions.assertEquals(feed.getUnitMarginBidDiscounted(), 4500d);
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
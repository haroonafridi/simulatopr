package com.hkcapital.portoflio.repository;

import com.hkcapital.portoflio.model.LiveInstrumentFeed;
import com.hkcapital.portoflio.repository.liveinstrumentfeed.LiveInstrumentFeedRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

@SpringBootTest
class LiveInstrumentFeedRepositoryTest
{

    @Autowired
    private LiveInstrumentFeedRepository liveInstrumentFeedRepository;

    private static final Instant FEED_DATE =
            Instant.parse("2026-01-01T10:00:00Z");

    private static final Instant FEED_CREATION_DATE =
            Instant.parse("2026-01-01T10:00:00Z");

    @Test
    public void shouldCreatLiveIntrumentFeedInDb()
    {

        LiveInstrumentFeed feed = LiveInstrumentFeed.builder().instrumentId(18)//
                .ask(5000d).bid(5001d).allowBuy(true).allowSell(false).askDiscounted(0d)//
                .availabilityReason(0).bidDiscounted(0d).conversionRateAsk(1d) //
                .feedDate(FEED_DATE)
                .creationDate(FEED_CREATION_DATE)//
                .conversionRateBid(1d).isInstrumentActive(true).delayedAsk(1).isMarketOpen(true)//
                .priceRateId(1l).delayedBid(4500).isExchangeOpen(true).isOfficialClosingPrice(true)//
                .lastExecution(5000d).maxPositionUnits(1).newUnitMargin(1d)//
                .officialClosingPrice(5000d).unitMarginAsk(4500d) //
                .unitMarginBid(4500d)
                .unitMarginAskDiscounted(4500d)//
                .unitMarginBidDiscounted(4500d).build();

        LiveInstrumentFeed persistedRate = liveInstrumentFeedRepository.save(feed);

        Assertions.assertTrue(persistedRate.getId() > 0);
        Assertions.assertEquals(persistedRate.getInstrumentId(), 18);
        Assertions.assertEquals(persistedRate.getAsk(), 5000d);
        Assertions.assertEquals(persistedRate.getBid(), 5001d);
        Assertions.assertTrue(persistedRate.getAllowBuy());
        Assertions.assertFalse(persistedRate.getAllowSell());
        Assertions.assertEquals(persistedRate.getAskDiscounted(), 0d);
        Assertions.assertEquals(persistedRate.getAvailabilityReason(), 0);
        Assertions.assertEquals(persistedRate.getBidDiscounted(), 0);
        Assertions.assertEquals(persistedRate.getConversionRateAsk(), 1);
        Assertions.assertEquals(persistedRate.getFeedDate(), FEED_DATE);
        Assertions.assertEquals(persistedRate.getCreationDate(), FEED_CREATION_DATE);
        Assertions.assertEquals(persistedRate.getConversionRateBid(), 1d);
        Assertions.assertTrue(persistedRate.getIsInstrumentActive());
        Assertions.assertEquals(persistedRate.getDelayedAsk(), 1);
        Assertions.assertTrue(persistedRate.getIsMarketOpen());
        Assertions.assertEquals(persistedRate.getPriceRateId(), 1L);
        Assertions.assertEquals(persistedRate.getDelayedBid(), 4500);
        Assertions.assertTrue(persistedRate.getIsExchangeOpen());
        Assertions.assertTrue(persistedRate.getIsOfficialClosingPrice());
        Assertions.assertEquals(persistedRate.getLastExecution(), 5000d);
        Assertions.assertEquals(persistedRate.getMaxPositionUnits(), 1);
        Assertions.assertEquals(persistedRate.getNewUnitMargin(), 1);
        Assertions.assertEquals(persistedRate.getOfficialClosingPrice(), 5000d);
        Assertions.assertEquals(persistedRate.getUnitMarginAsk(), 4500d);
        Assertions.assertEquals(persistedRate.getUnitMarginBid(), 4500d);
        Assertions.assertEquals(persistedRate.getUnitMarginAskDiscounted(), 4500d);
        Assertions.assertEquals(persistedRate.getUnitMarginBidDiscounted(), 4500d);
    }

}
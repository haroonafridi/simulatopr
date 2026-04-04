package com.hkcapital.portoflio.service.impl;

import com.hkcapital.portoflio.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portoflio.model.LiveInstrumentFeed;
import com.hkcapital.portoflio.repository.LiveInstrumentFeedRepository;
import com.hkcapital.portoflio.service.MarketFeedSubscriber;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.logging.Logger;

@Service
public class MarketFeedDbWriter implements MarketFeedSubscriber
{
    private Logger logger = Logger.getLogger(MarketFeedDbWriter.class.getName());
    private final LiveInstrumentFeedRepository liveInstrumentFeedRepository;

    public MarketFeedDbWriter(LiveInstrumentFeedRepository liveInstrumentFeedRepository)
    {
        this.liveInstrumentFeedRepository = liveInstrumentFeedRepository;
    }

    @Override
    public void process(LiveInstrumentRate liveRate)
    {
        LiveInstrumentFeed liveInstrumentFeed = //
                LiveInstrumentFeed.builder()
                        .instrumentId(liveRate.getInstrumentId())//
                        .ask(liveRate.getAsk()).bid(liveRate.getBid())
                        .allowBuy(liveRate.getAllowBuy())
                        .allowSell(liveRate.getAllowSell())
                        .askDiscounted(liveRate.getAskDiscounted())//
                        .availabilityReason(liveRate.getAvailabilityReason())
                        .bidDiscounted(liveRate.getBidDiscounted())
                        .conversionRateAsk(liveRate.getConversionRateAsk())
                        .creationDate(Instant.now())//
                        .conversionRateBid(liveRate.getConversionRateBid())
                        .isInstrumentActive(liveRate.getIsInstrumentActive())
                        .delayedAsk(liveRate.getDelayedAsk())
                        .isMarketOpen(liveRate.getIsMarketOpen())//
                        .priceRateId(liveRate.getPriceRateId())
                        .delayedBid(liveRate.getDelayedBid())
                        .isExchangeOpen(liveRate.getIsExchangeOpen())
                        .isOfficialClosingPrice(liveRate.getIsOfficialClosingPrice())//
                        .lastExecution(liveRate.getLastExecution())
                        .maxPositionUnits(liveRate.getMaxPositionUnits())
                        .newUnitMargin(liveRate.getNewUnitMargin())//
                        .officialClosingPrice(liveRate.getOfficialClosingPrice())
                        .unitMarginAsk(liveRate.getUnitMarginAsk())
                        .unitMarginBid(liveRate.getUnitMarginBid())
                        .unitMarginAskDiscounted(liveRate.getUnitMarginAskDiscounted())//
                        .unitMarginBidDiscounted(liveRate.getUnitMarginBidDiscounted()).build();
        liveInstrumentFeedRepository.save(liveInstrumentFeed);
    }
}

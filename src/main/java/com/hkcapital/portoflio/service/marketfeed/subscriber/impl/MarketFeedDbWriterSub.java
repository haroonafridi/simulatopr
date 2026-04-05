package com.hkcapital.portoflio.service.marketfeed.subscriber.impl;

import com.hkcapital.portoflio.model.LiveInstrumentFeed;
import com.hkcapital.portoflio.repository.liveinstrumentfeed.LiveInstrumentFeedRepository;
import com.hkcapital.portoflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portoflio.service.marketfeed.subscriber.MarketFeedSubscriber;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class MarketFeedDbWriterSub implements MarketFeedSubscriber
{
    private final LiveInstrumentFeedRepository liveInstrumentFeedRepository;

    public MarketFeedDbWriterSub(LiveInstrumentFeedRepository liveInstrumentFeedRepository)
    {
        this.liveInstrumentFeedRepository = liveInstrumentFeedRepository;
    }

    @Override
    public void process(LiveInstrumentRate liveRate)
    {

        if (liveRate.getInstrumentId() != null && liveRate.getAsk() != null && liveRate.getBid() != null)
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
                            .feedDate(liveRate.getDate())
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
}

package com.hkcapital.portoflio.service.impl;

import com.hkcapital.portoflio.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portoflio.model.LiveInstrumentFeed;
import com.hkcapital.portoflio.repository.LiveInstrumentFeedRepository;
import com.hkcapital.portoflio.service.MarketFeedSubscriber;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class LiveInstrumentFeedDbServiceImpl implements LiveInstrumentFeedDbService<LiveInstrumentRate> , MarketFeedSubscriber
{
    private final LiveInstrumentFeedRepository feedRepository;

    public LiveInstrumentFeedDbServiceImpl(LiveInstrumentFeedRepository feedRepository)
    {
        this.feedRepository = feedRepository;
    }

    @Override
    public LiveInstrumentFeed save(LiveInstrumentRate liveInstrumentRate)
    {
        return feedRepository.save(createLiveInstrumentFeed(liveInstrumentRate));
    }

    private LiveInstrumentFeed createLiveInstrumentFeed(LiveInstrumentRate liveInstrumentRate)
    {

        return LiveInstrumentFeed.builder().instrumentId(liveInstrumentRate.getInstrumentId())//
                .ask(liveInstrumentRate.getAsk()).bid(liveInstrumentRate.getBid())
                .allowBuy(liveInstrumentRate.getAllowBuy()).allowSell(liveInstrumentRate.getAllowSell())
                .askDiscounted(liveInstrumentRate.getAskDiscounted())//
                .availabilityReason(liveInstrumentRate.getAvailabilityReason())
                .bidDiscounted(liveInstrumentRate.getBidDiscounted())
                .conversionRateAsk(liveInstrumentRate.getConversionRateAsk()) //
                .feedDate(liveInstrumentRate.getDate())
                .creationDate(Instant.now())//
                .conversionRateBid(liveInstrumentRate.getConversionRateBid())
                .isInstrumentActive(liveInstrumentRate.getIsInstrumentActive())
                .delayedAsk(liveInstrumentRate.getDelayedAsk())
                .isMarketOpen(liveInstrumentRate.getIsMarketOpen())//
                .priceRateId(liveInstrumentRate.getPriceRateId())
                .delayedBid(liveInstrumentRate.getDelayedBid())
                .isExchangeOpen(liveInstrumentRate.getIsExchangeOpen())
                .isOfficialClosingPrice(liveInstrumentRate.getIsOfficialClosingPrice())//
                .lastExecution(liveInstrumentRate.getLastExecution())
                .maxPositionUnits(liveInstrumentRate.getMaxPositionUnits())
                .newUnitMargin(liveInstrumentRate.getNewUnitMargin())//
                .officialClosingPrice(liveInstrumentRate.getOfficialClosingPrice())
                .unitMarginAsk(liveInstrumentRate.getUnitMarginAsk()) //
                .unitMarginBid(liveInstrumentRate.getUnitMarginBid())
                .unitMarginAskDiscounted(liveInstrumentRate.getUnitMarginAskDiscounted())//
                .unitMarginBidDiscounted(liveInstrumentRate.getUnitMarginBidDiscounted())
                .build();
    }

    @Override
    public void process(LiveInstrumentRate liveInstrumentRate)
    {
        save(liveInstrumentRate);
    }
}

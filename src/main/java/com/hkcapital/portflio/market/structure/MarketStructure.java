package com.hkcapital.portflio.market.structure;

import com.hkcapital.portflio.model.Candle;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portflio.service.candle.etoro.impl.SignalBuilder;
import com.hkcapital.portflio.service.marketfeed.subscriber.MarketFeedSubscriber;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.NavigableSet;

@Getter
public class MarketStructure implements MarketFeedSubscriber
{
    private final PreviousDayMarketRange previousDayMarketRange;
    private final MarketSession marketSession;
    private final int intervals;
    private NavigableSet<MarketPriceBand> upperBands;
    private NavigableSet<MarketPriceBand> lowerBands;
    private final Range range;
    private boolean initCompleted = false;

    @Builder
    public MarketStructure(final PreviousDayMarketRange previousDayMarketRange, //
                           final MarketSession marketSession, //
                           final int intervals,
                           final Modus modus
                                    )
    {
        this.previousDayMarketRange = previousDayMarketRange;
        this.marketSession = marketSession;
        this.intervals = intervals;
        this.range = RangeExtractor.of(previousDayMarketRange, modus);
        upperBands = BandGenerator.of(range, BandType.HIGH, intervals);
        lowerBands = BandGenerator.of(range, BandType.LOW, intervals);
    }

    @Override
    public void process(LiveInstrumentRate liveInstrumentRate, SignalBuilder signalBuilder)
    {
        // process logic goes here
    }

    public void updateBands(Candle candle)
    {
        // fillbands here
    }

    public void init(final List<Candle> candles)
    {
        if (initCompleted)
        {
            return;
        }

        initCompleted = true;

        for (Candle candle : candles)
        {
            final double low = candle.getLow();
            final double high = candle.getHigh();

            // HIGH bands (use candle high)
            for (MarketPriceBand upperBand : upperBands)
            {
                if (high >= upperBand.getLowerBound()
                        && high < upperBand.getUpperBound())
                {
                    upperBand.updateMarketVisitCount(
                            upperBand.getMarketVisitCount() + 1
                    );
                    break; // important optimization
                }
            }

            // LOW bands (use candle low)
            for (MarketPriceBand lowerBand : lowerBands)
            {
                if (low >= lowerBand.getLowerBound()
                        && low < lowerBand.getUpperBound())
                {
                    lowerBand.updateMarketVisitCount(
                            lowerBand.getMarketVisitCount() + 1
                    );
                    break; // important optimization
                }
            }
        }
    }

}

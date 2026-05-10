package com.hkcapital.portflio.market.structure;

import com.hkcapital.portflio.model.Candle;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portflio.service.candle.etoro.impl.SignalBuilder;
import com.hkcapital.portflio.service.marketfeed.subscriber.MarketFeedSubscriber;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.NavigableSet;
import java.util.Optional;

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
    private Modus modus;

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
        this.modus = modus;
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
        updateBand(upperBands, candle, candle.getHigh());
        updateBand(lowerBands, candle, candle.getLow());
    }

    private void updateBand(NavigableSet<MarketPriceBand> upperBands,Candle candle, double price)
    {
        findMarketPriceBand(upperBands, price)
                .ifPresentOrElse(marketPriceBand ->
                {
                    updateMarketVisitCount(upperBands, price);
                }, () ->
                {
                    createNewBands(candle);
                    updateMarketVisitCount(upperBands, price);
                });
    }

    private void createNewBands(Candle candle)
    {
        final CandlePriceRange candlePriceRange =
                CandlePriceRange.builder()
                        .candle(candle).build();
        final Range newRange = RangeExtractor.of(candlePriceRange, modus);
        final NavigableSet<MarketPriceBand> newUpperBands = //
                BandGenerator.of(newRange, BandType.HIGH, intervals);
        final NavigableSet<MarketPriceBand> newLowerBands = //
                BandGenerator.of(newRange, BandType.LOW, intervals);

        for (MarketPriceBand band : newUpperBands)
        {
            upperBands.add(band);
        }
        // merge LOW bands
        for (MarketPriceBand band : newLowerBands)
        {
            lowerBands.add(band);
        }
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
            updateMarketVisitCount(upperBands, high);
            updateMarketVisitCount(lowerBands, low);
        }
    }

    private void updateMarketVisitCount(NavigableSet<MarketPriceBand> bands, double price)
    {
        for (MarketPriceBand upperBand : bands)
        {
            if (price >= upperBand.getLowerBound()
                    && price < upperBand.getUpperBound())
            {
                upperBand.updateMarketVisitCount(
                        upperBand.getMarketVisitCount() + 1
                );
                break; // important optimization
            }
        }
    }

    private Optional<MarketPriceBand> findMarketPriceBand(
            NavigableSet<MarketPriceBand> bands,
            double price)
    {
        for (MarketPriceBand band : bands)
        {
            if (price >= band.getLowerBound()
                    && price < band.getUpperBound())
            {
                return Optional.of(band);
            }
        }
        return Optional.empty();
    }

}

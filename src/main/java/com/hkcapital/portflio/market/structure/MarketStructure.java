package com.hkcapital.portflio.market.structure;

import com.hkcapital.portflio.model.Candle;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portflio.service.candle.etoro.impl.SignalBuilder;
import com.hkcapital.portflio.service.marketfeed.subscriber.MarketFeedSubscriber;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.NavigableSet;
import java.util.Optional;

@Getter
@Slf4j
public class MarketStructure implements MarketFeedSubscriber , Flushable
{
    private final Logger logger = LoggerFactory.getLogger(MarketStructure.class);
    private final PriceRange priceRange;
    private final MarketSession marketSession;
    private final int intervals;
    private NavigableSet<MarketPriceBand> upperBands;
    private NavigableSet<MarketPriceBand> lowerBands;
    private final Range range;
    @Getter
    private boolean initCompleted = false;
    private Modus modus;
    @Getter
    private MarketAction action;
    @Getter
    private MarketRegime marketRegime;

    @Builder
    public MarketStructure(final PriceRange priceRange, //
                           final MarketSession marketSession, //
                           final int intervals,
                           final Modus modus
    )
    {
        this.priceRange = priceRange;
        this.marketSession = marketSession;
        this.intervals = intervals;
        this.modus = modus;
        this.range = RangeExtractor.of(priceRange, modus);
        upperBands = BandGenerator.of(range, BandType.HIGH, intervals);
        lowerBands = BandGenerator.of(range, BandType.LOW, intervals);
    }

    @Override
    public void process(LiveInstrumentRate liveInstrumentRate, SignalBuilder signalBuilder)
    {
        // process logic goes here
        // update action here based on conditions
        // update market regime here based on conditions
    }

    public void createOrUpdateBands(Candle candle)
    {
        updateBand(upperBands, candle, candle.getHigh());
        updateBand(lowerBands, candle, candle.getLow());

        if(lowerBands != null) {
            lowerBands.forEach(b -> logger.info(b.toString()));
        }

        if(upperBands != null) {
            upperBands.forEach(b -> logger.info(b.toString()));
        }
    }

    private void updateBand(NavigableSet<MarketPriceBand> band, Candle candle, double price)
    {
        findMarketPriceBand(band, price)
                .ifPresentOrElse(marketPriceBand ->
                {
                    updateMarketVisitCount(band, price);
                }, () ->
                {
                    createNewBands(candle);
                    updateMarketVisitCount(band, price);
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
        // merge HIGH bands
        addBands(newUpperBands, upperBands);
        // merge LOW bands
        addBands(newLowerBands, lowerBands);
    }

    private void addBands(NavigableSet<MarketPriceBand> newBands,
                          NavigableSet<MarketPriceBand> existingBands)
    {
        for (MarketPriceBand band : newBands)
        {
            existingBands.add(band);
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
            // LOW bands (use candle low)
            updateMarketVisitCount(lowerBands, low);
        }

        if(lowerBands != null) {
            lowerBands.forEach(b -> logger.info(b.toString()));
        }

        if(upperBands != null) {
            upperBands.forEach(b -> logger.info(b.toString()));
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

    @Override
    public void flush()
    {
        upperBands.clear(); // flush logic here
        upperBands.clear(); //
        initCompleted = false;
    }


}

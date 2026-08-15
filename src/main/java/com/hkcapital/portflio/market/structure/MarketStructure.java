package com.hkcapital.portflio.market.structure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.model.Candle;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portflio.service.candle.etoro.impl.SignalBuilder;
import com.hkcapital.portflio.service.marketfeed.subscriber.MarketFeedSubscriber;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NavigableSet;
import java.util.Optional;

@Getter
@Slf4j
public class MarketStructure implements MarketFeedSubscriber, Flushable
{
    private final Logger logger = LoggerFactory.getLogger(MarketStructure.class);
    private final PriceRange priceRange;
    private final MarketSession marketSession;
    private final LocalDate creationDate = LocalDate.now();
    private final LocalDate marketDate;
    private final int intervals;
    private NavigableSet<MarketPriceBand> upperBands;
    private NavigableSet<MarketPriceBand> lowerBands;
    private final Range range;
    private final MarketStructure childMarketStructure;
    @Getter
    private final OrderCache orderCache;
    @Getter
    private boolean initCompleted = false;
    private Modus modus;
    @Getter
    private MarketAction marketAction = MarketAction.NONE;
    @Getter
    private MarketRegime marketRegime;
    @Getter
    final Integer timeFrame;
    @Getter
    final TimeFramesUnit timeFrameUnit;
    @Getter
    private MarketTypes marketTypes;
    @Getter
    private Instrument instrument;
    int sellSignal = 0;
    int buySignal = 0;
    int totalTicks = 1;
    private final ObjectMapper objectMapper;

    @Builder
    public MarketStructure(final PriceRange priceRange, //
                           final MarketSession marketSession, //
                           final int intervals,
                           final Modus modus,
                           final MarketTypes marketTypes,
                           final Instrument instrument,
                           final ObjectMapper objectMapper,
                           final Integer timeFrame,
                           final TimeFramesUnit timeFrameUnit,
                           final MarketStructure childMarketStructure,
                           final LocalDate marketDate
    )
    {
        this.priceRange = priceRange;
        this.marketSession = marketSession;
        this.intervals = intervals;
        this.modus = modus;
        this.range = RangeExtractor.of(priceRange, modus);
        this.instrument = instrument;
        this.marketTypes = marketTypes;
        this.timeFrame = timeFrame;
        this.timeFrameUnit = timeFrameUnit;
        upperBands = BandGenerator.of(range, BandType.HIGH, intervals, timeFrame, timeFrameUnit);
        lowerBands = BandGenerator.of(range, BandType.LOW, intervals, timeFrame, timeFrameUnit);
        this.childMarketStructure = childMarketStructure;
        this.objectMapper = objectMapper;
        this.marketDate = marketDate;
        orderCache = new OrderCache();
    }

    @Override
    public void process(LiveInstrumentRate liveInstrumentRate, SignalBuilder signalBuilder)
    {

        final double pTAbove = PriceTolerance.getPriceToleranceAbove(marketTypes, instrument);

        final double pTBelow = PriceTolerance.getPriceToleranceBelow(marketTypes, instrument);

        MarketStructureMap ub = new MarketStructureMap(upperBands, liveInstrumentRate.getAsk());

        MarketStructureMap lb = new MarketStructureMap(lowerBands, liveInstrumentRate.getAsk());

        Optional<MarketPriceBand> hB1 = ub.findMaxUpperBoundOf(BandType.HIGH);

        Optional<MarketPriceBand> hB2 = ub.findNthLeastVisitedHighBand(1);

        Optional<MarketPriceBand> lB1 = lb.findNthMostVisitedLowBand(0);

        Optional<MarketPriceBand> lB2 = lb.findNthMostVisitedLowBand(1);

        Distance dl1 = getDistance(liveInstrumentRate.getAsk(), lB1.get().getLowerBound());

        orderCache.process(liveInstrumentRate, objectMapper);
    }


    private Distance getDistance(Double currentPrice, Double referencePrice)
    {
        return Distance.builder()
                .currentPrice(currentPrice)
                .referencePrice(referencePrice).build();
    }

    public void createOrUpdateBands(Candle candle)
    {
        Instant high = candle.getHighTime() == null ? Instant.now() : candle.getHighTime();
        Instant low = candle.getLowTime() == null ? Instant.now() : candle.getLowTime();
        updateBand(upperBands, candle, candle.getHigh(), high);
        updateBand(lowerBands, candle, candle.getLow(), low);

        if (lowerBands != null)
        {
            lowerBands.forEach(b -> logger.info(b.toString()));
        }

        if (upperBands != null)
        {
            upperBands.forEach(b -> logger.info(b.toString()));
        }
    }

    private void updateBand(NavigableSet<MarketPriceBand> band, Candle candle, double price, Instant highOrLow)
    {
        findMarketPriceBand(band, price)
                .ifPresentOrElse(marketPriceBand ->
                {
                    updateMarketVisitCountAndTime(band, price, highOrLow);
                }, () ->
                {
                    createNewBands(candle);
                    updateMarketVisitCountAndTime(band, price, highOrLow);
                });
    }

    private void createNewBands(Candle candle)
    {
        final CandlePriceRange candlePriceRange =
                CandlePriceRange.builder()
                        .candle(candle).build();
        final Range newRange = RangeExtractor.of(candlePriceRange, modus);
        final NavigableSet<MarketPriceBand> newUpperBands = //
                BandGenerator.of(newRange, BandType.HIGH, intervals, candle.getTimeFrame(), TimeFramesUnit.valueOf(candle.getTimeFrameUnit()));
        final NavigableSet<MarketPriceBand> newLowerBands = //
                BandGenerator.of(newRange, BandType.LOW, intervals, candle.getTimeFrame(), TimeFramesUnit.valueOf(candle.getTimeFrameUnit()));
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
            updateMarketVisitCountAndTime(upperBands, high, candle.getHighTime());
            // LOW bands (use candle low)
            updateMarketVisitCountAndTime(lowerBands, low, candle.getLowTime());

        }

        if (lowerBands != null)
        {
            try
            {
                logger.info(objectMapper.writeValueAsString(lowerBands));
            } catch (JsonProcessingException e)
            {
                logger.info("Error in logging lower bands");
            }
        }

        if (upperBands != null)
        {
            try
            {
                logger.info(objectMapper.writeValueAsString(upperBands));
            } catch (JsonProcessingException e)
            {
                logger.info("Error in logging upper bands");
            }
        }
    }

    private void updateMarketVisitCountAndTime(NavigableSet<MarketPriceBand> bands, double price, Instant highLowTime)
    {
        for (MarketPriceBand band : bands)
        {
            if (price >= band.getLowerBound()
                    && price < band.getUpperBound())
            {
                band.updateMarketVisitCount(
                        band.getMarketVisitCount() + 1
                );
                if (highLowTime == null)
                {
                    band.updateTime(Instant.now());
                } else
                {
                    band.updateTime(highLowTime);
                }
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

    public LocalDate getCreationDate()
    {
        return creationDate;
    }

    @Override
    public void flush()
    {
        upperBands.clear(); // flush logic here
        lowerBands.clear(); //
        initCompleted = false;
    }


}

package com.hkcapital.portflio.market.structure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final int intervals;
    private NavigableSet<MarketPriceBand> upperBands;
    private NavigableSet<MarketPriceBand> lowerBands;
    private final Range range;

    private final OrderCache orderCache;
    @Getter
    private boolean initCompleted = false;
    private Modus modus;
    @Getter
    private MarketAction marketAction = MarketAction.NONE;
    @Getter
    private MarketRegime marketRegime;

    private MarketTypes marketTypes;

    private Instrument instrument;
    int sellSignal = 1;
    int buySignal = 1;

    int totalTicks = 1;
    private final ObjectMapper objectMapper;

    @Builder
    public MarketStructure(final PriceRange priceRange, //
                           final MarketSession marketSession, //
                           final int intervals,
                           final Modus modus,
                           final MarketTypes marketTypes,
                           final Instrument instrument,
                           final ObjectMapper objectMapper
    )
    {
        this.priceRange = priceRange;
        this.marketSession = marketSession;
        this.intervals = intervals;
        this.modus = modus;
        this.range = RangeExtractor.of(priceRange, modus);
        this.instrument = instrument;
        this.marketTypes = marketTypes;
        upperBands = BandGenerator.of(range, BandType.HIGH, intervals);
        lowerBands = BandGenerator.of(range, BandType.LOW, intervals);
        this.objectMapper = objectMapper;
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

        Distance dh1 = getDistance(liveInstrumentRate.getAsk(), hB1.get().getUpperBound());

        Distance dh2 = getDistance(liveInstrumentRate.getAsk(), hB2.get().getUpperBound());

        Distance dl1 = getDistance(liveInstrumentRate.getAsk(), lB1.get().getLowerBound());

        Distance dl2 = getDistance(liveInstrumentRate.getAsk(), lB2.get().getLowerBound());

        // buy side
        double sl = lB1.get().getLowerBound() - (2 * pTBelow);

        double tp = hB1.get().getUpperBound() - (2 * pTAbove);

        if (dh1.isBelow())
        {
            if (dh1.absPoints() <= pTBelow && pTBelow >= dh1.absPoints())
            {
                OrderLogger orderLogger =
                        OrderLogger.builder()
                                .marketPriceBand(hB1.get()).distance(dh1)
                                .absPoint(dh1.absPoints())
                                .orderType(OrderType.SELL)
                                .pTBelow(pTBelow)
                                .orderCount(sellSignal)
                                .instant(Instant.now())
                                .price(liveInstrumentRate.getAsk())
                                .build();
                String order = null;
                try
                {
                    order = objectMapper.writeValueAsString(orderLogger);
                } catch (JsonProcessingException e)
                {
                    logger.info("cannot write sell to logger");
                }
                logger.info("{}", order);
                Order sellOrder = Order.builder()
                        .orderType(OrderType.SELL)
                        .openPrice(liveInstrumentRate.getAsk())
                        .tp(liveInstrumentRate.getAsk()-10d)
                        .sl(liveInstrumentRate.getAsk()+10)
                        .status("OPEN")
                        .info("SELL order opend at price ["+liveInstrumentRate.getAsk()+"]")
                        .leverage(20)
                        .build();
                orderCache.register(OrderType.SELL.getValue()+"-"+sellSignal, sellOrder);
                sellSignal = sellSignal + 1;
            }
        }

        if (dl1.isAbove())
        {
            logger.info(" points above = {}, top = {} , price = {} ", dl1.absPoints(), pTAbove, liveInstrumentRate.getAsk());
            if (dl1.absPoints() >= pTAbove && pTAbove >= dl1.absPoints())
            {

                OrderLogger orderLogger =
                        OrderLogger.builder()
                                .marketPriceBand(lB1.get()).distance(dl1)
                                .absPoint(dl1.absPoints())
                                .orderType(OrderType.BUY)
                                .pTBelow(pTAbove)
                                .orderCount(buySignal)
                                .instant(Instant.now())
                                .price(liveInstrumentRate.getAsk())
                                .build();
                try
                {
                    String order = objectMapper.writeValueAsString(orderLogger);
                    logger.info("{}", order);
                } catch (JsonProcessingException e)
                {
                    logger.info("cannot write buy order to logger");
                }
                Order sellOrder = Order.builder()
                        .orderType(OrderType.BUY)
                        .openPrice(liveInstrumentRate.getAsk())
                        .tp(liveInstrumentRate.getAsk()+10d)
                        .sl(liveInstrumentRate.getAsk()-10)
                        .leverage(20)
                        .status("OPEN")
                        .info("BUY order opened at price ["+liveInstrumentRate.getAsk()+"]")
                        .build();
                buySignal = buySignal + 1;
                orderCache.register(OrderType.BUY.getValue()+"-"+sellSignal, sellOrder);
            }
        }

        orderCache.process(liveInstrumentRate,objectMapper);
    }


    private Distance getDistance(Double currentPrice, Double referencePrice)
    {
        return Distance.builder()
                .currentPrice(currentPrice)
                .referencePrice(referencePrice).build();
    }

    public void createOrUpdateBands(Candle candle)
    {
        updateBand(upperBands, candle, candle.getHigh());
        updateBand(lowerBands, candle, candle.getLow());

        if (lowerBands != null)
        {
            lowerBands.forEach(b -> logger.info(b.toString()));
        }

        if (upperBands != null)
        {
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

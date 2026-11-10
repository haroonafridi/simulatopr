package com.hkcapital.portflio.service.orders;

import com.hkcapital.portflio.market.indicators.CandleBuilder;
import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.market.structure.BandType;
import com.hkcapital.portflio.market.structure.MarketPriceBand;
import com.hkcapital.portflio.market.structure.MarketStructure;
import com.hkcapital.portflio.market.structure.MarketTypes;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.InstrumentMarketStructureConf;
import com.hkcapital.portflio.model.Strategy;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portflio.service.candle.etoro.impl.SignalBuilder;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.instrumentmarketstructureconf.InstrumentMarketStructureConfService;
import com.hkcapital.portflio.service.marketconditions.MarketConditionsService;
import com.hkcapital.portflio.service.strategy.StrategyService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class DynamicOrderProcessor implements OrderProcessor
{
    private final InstrumentService instrumentService;
    private final StrategyService strategyService;
    private final InstrumentMarketStructureConfService instMarktStrConfService;


    public DynamicOrderProcessor
            (
                    final InstrumentService instrumentService,
                    final StrategyService strategyService,
                    final InstrumentMarketStructureConfService instMarktStrConfService
            )
    {
        this.instrumentService = instrumentService;
        this.strategyService = strategyService;
        this.instMarktStrConfService = instMarktStrConfService;
    }

    @Override
    public void process(final LiveInstrumentRate liveInstrumentRate, final SignalBuilder signalBuilder)
    {

        final Strategy strategy = strategyService.findDynamicStrategies(true).stream().findFirst().get();

        if (!strategy.getActive())
        {
            log.info("Skip dynamic orders, no active strategy found.");
            return;
        }

        final Instrument inst = instrumentService
                .findByActiveAndWithFeedAndWithCandleAndWithDynamicOrders
                        (
                                true,
                                true,
                                true,
                                true
                        ).get(0);

        final CandleBuilder fifteenMinutesCandlesBuilder =
                signalBuilder.getCandlesBuilderByInstrumentAndByTimeFrameAndTimeUnit
                        (
                                inst,
                                15,
                                "MINUTE"
                        );

        final MarketStructure gold4HourStr = fifteenMinutesCandlesBuilder
                .getMarketStructureManagerCache()
                .getStructures()
                .get(MarketTypes.GOLD_4_HOUR.getValue());


        //Main Market ,
        final InstrumentMarketStructureConf mainMarket = //
                instMarktStrConfService//
                        .findByStructureName(MarketTypes.GOLD_5_MIN.getValue())
                        .stream()
                        .findFirst()//
                        .get();

        //Trending Market
        final InstrumentMarketStructureConf trendMarket = //
                instMarktStrConfService.findByStructureName(mainMarket.getTrendStructureName())
                        .stream()
                        .findFirst().get();

        //Signal Market and Trend direction
        final MarketStructure childStructure = gold4HourStr //
                .getChildStructureByTimeFrameAndTimeUnit //
                        (gold4HourStr, TimeFramesUnit.MINUTE.getUnit(), 1);

        final List<MarketPriceBand> supportBands = childStructure //
                .findMarketPriceBandByDepthAndBandType
                        (
                                BandType.LOW,
                                mainMarket.getOrderBlockDepth()
                        );

        final List<MarketPriceBand> resistanceBands = childStructure //
                .findMarketPriceBandByDepthAndBandType
                        (
                                BandType.HIGH,
                                mainMarket.getOrderBlockDepth()
                        );

        final CandleBuilder trendIngMarketIndicators =
                signalBuilder.getCandlesBuilderByInstrumentAndByTimeFrameAndTimeUnit
                        (
                                inst,
                                1,
                                "MINUTE"
                        );

        final IndicatorProcessor EMA_9_1_MINUTE = trendIngMarketIndicators.getIndicator("EMA_9_1_MINUTE");
        final IndicatorProcessor EMA_20_1_MINUTE = trendIngMarketIndicators.getIndicator("EMA_20_1_MINUTE");
        final IndicatorProcessor EMA_50_1_MINUTE = trendIngMarketIndicators.getIndicator("EMA_50_1_MINUTE");
        final IndicatorProcessor EMA_100_1_MINUTE = trendIngMarketIndicators.getIndicator("EMA_100_1_MINUTE");
        final IndicatorProcessor SMA_9_1_MINUTE = trendIngMarketIndicators.getIndicator("SMA_9_1_MINUTE");
        final IndicatorProcessor SMA_20_1_MINUTE = trendIngMarketIndicators.getIndicator("SMA_20_1_MINUTE");
        final IndicatorProcessor SMA_50_1_MINUTE = trendIngMarketIndicators.getIndicator("SMA_50_1_MINUTE");
        final IndicatorProcessor SMA_100_1_MINUTE = trendIngMarketIndicators.getIndicator("SMA_100_1_MINUTE");

        final IndicatorProcessor ATR_20_1_MINUTE = trendIngMarketIndicators.getIndicator("ATR_20_1_MINUTE");

        final IndicatorProcessor RSI_20_1_MINUTE = trendIngMarketIndicators.getIndicator("RSI_9_1_MINUTE");

        final EMA ema = EMA.Of
                (
                        EMA_9_1_MINUTE.getValue(), //
                        EMA_20_1_MINUTE.getValue(), //
                        EMA_50_1_MINUTE.getValue(), //
                        EMA_100_1_MINUTE.getValue()
                );


        final SMA sma = SMA.Of
                (
                        SMA_9_1_MINUTE.getValue(), //
                        SMA_20_1_MINUTE.getValue(), //
                        SMA_50_1_MINUTE.getValue(), //
                        SMA_100_1_MINUTE.getValue() //
                );

        final double atr = ATR_20_1_MINUTE.getValue();

        final double rsi = RSI_20_1_MINUTE.getValue();


        final MarketPriceBand support0 = supportBands.get(0);
        final MarketPriceBand support1 = supportBands.get(1);
        final MarketPriceBand support2 = supportBands.get(2);

        final MarketPriceBand resistance0 = resistanceBands.get(0);
        final MarketPriceBand resistance1 = resistanceBands.get(1);
        final MarketPriceBand resistance2 = resistanceBands.get(2);

        final Region rr1 = Region.of
                (
                        resistance0.getLowerBound(), //
                        resistance0.getUpperBound(), //
                        resistance0.getMarketVisitCount()
                );


        final Region rr2 = Region.of
                (
                        resistance1.getLowerBound(),
                        resistance1.getUpperBound(),
                        resistance1.getMarketVisitCount()
                );

        final Region rr3 = Region.of
                (
                        resistance2.getLowerBound(),
                        resistance2.getUpperBound(),
                        resistance2.getMarketVisitCount()
                );


        final Resistance resistance = Resistance.of
                (
                        rr1,
                        rr2,
                        rr3
                );

        final Region sr1 = Region.of
                (
                        support0.getLowerBound(),
                        support0.getUpperBound(),
                        support0.getMarketVisitCount()
                );

        final Region sr2 = Region.of
                (
                        support1.getLowerBound(),
                        support1.getUpperBound(),
                        support1.getMarketVisitCount()
                );

        final Region sr3 = Region.of
                (
                        support2.getLowerBound(),
                        support2.getUpperBound(),
                        support0.getMarketVisitCount()
                );

        final Support support = Support.of
                (
                        sr1,
                        sr2,
                        sr3
                );

        final TrendIndicatorSignal trend = //
                TrendIndicatorSignal.of
                        (
                                TimeFramesUnit.MINUTE.getUnit(),
                                5,
                                rsi,
                                atr,
                                ema,
                                sma,
                                support,
                                resistance
                        );

        final TrendIndicatorSignal oldTrend =
                fifteenMinutesCandlesBuilder //
                        .getMarketStructureManagerCache()
                        .getTrendTopic()
                        .updateTrend(trend);


    }

}

package com.hkcapital.portflio.service.orders;

import com.hkcapital.portflio.market.indicators.CandleBuilder;
import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.market.structure.MarketPriceBand;
import com.hkcapital.portflio.market.structure.MarketStructure;
import com.hkcapital.portflio.market.structure.MarketTypes;
import com.hkcapital.portflio.market.structure.PriceRange;
import com.hkcapital.portflio.model.*;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portflio.service.candle.etoro.impl.SignalBuilder;
import com.hkcapital.portflio.service.configuration.ConfigurationService;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.instrumentmarketstructureconf.InstrumentMarketStructureConfService;
import com.hkcapital.portflio.service.marketconditions.MarketConditionsService;
import com.hkcapital.portflio.service.positions.PositionService;
import com.hkcapital.portflio.service.positions.PositionType;
import com.hkcapital.portflio.service.srmatrix.SRMatrixService;
import com.hkcapital.portflio.service.strategy.StrategyService;
import com.hkcapital.portflio.util.LeverageCodes;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class DynamicOrderProcessor implements OrderProcessor
{
    private final InstrumentService instrumentService;
    private final ConfigurationService confService;
    private final StrategyService strategyService;
    private final SRMatrixService sRMatrixService;
    private final MarketConditionsService marktConditionsService;
    private final PositionService positionService;
    private final OrderManagerService orderManagerService;
    private final InstrumentMarketStructureConfService instMarktStrConfService;


    public DynamicOrderProcessor(final InstrumentService instrumentService,
                                 final ConfigurationService confService, //
                                 final StrategyService strategyService, //
                                 final SRMatrixService sRMatrixService, //
                                 final MarketConditionsService marktConditionsService,
                                 final PositionService positionService, //
                                 final OrderManagerService orderManagerService,
                                 final InstrumentMarketStructureConfService instMarktStrConfService)
    {
        this.instrumentService = instrumentService;
        this.confService = confService;
        this.strategyService = strategyService;
        this.sRMatrixService = sRMatrixService;
        this.marktConditionsService = marktConditionsService;
        this.positionService = positionService;
        this.orderManagerService = orderManagerService;
        this.instMarktStrConfService = instMarktStrConfService;
    }

    @Override
    public void process(LiveInstrumentRate liveInstrumentRate, SignalBuilder signalBuilder)
    {

        Strategy strategy = strategyService.findDynamicStrategies(true).stream().findFirst().get();

        if (!strategy.getActive())
        {
            log.info("Skip dynamic orders, no active strategy found.");
            return;
        }

        List<CandleBuilder> fourHourCandles = signalBuilder.getCandleBuilder()
                .stream()
                .filter(e -> e.getInterval().intValue() == 4
                        && e.getTimeFrame().equals(TimeFramesUnit.HOUR))
                .collect(Collectors.toList());

        CandleBuilder candleBuilderGold = fourHourCandles.stream()
                .filter(cb -> cb.getInstrument()
                        .getInstrumentTicker()
                        .equals("XAUUSD"))
                .findFirst().get();

        CandleBuilder candleBuilderNasdaq = fourHourCandles.stream()
                .filter(cb -> cb.getInstrument()
                        .getInstrumentTicker()
                        .equals("NSDQ100"))
                .findFirst().get();


        MarketStructure gold4HourStr =
                getStructureByTimeFrame(candleBuilderGold//
                                .getMarketStructureManagerCache().getStructures()
                                .get(MarketTypes.GOLD_4_HOUR.getValue()),
                        TimeFramesUnit.HOUR.getUnit(), 4);


        Instrument inst = gold4HourStr.getInstrument();

        PriceRange range = gold4HourStr.getPriceRange();

        MarketConditions marketConditions =
                MarketConditions.builder()
                        .instrument(inst)
                        .dayLow(range.getLow())
                        .dayHigh(range.getHigh())
                        .percentMove(0d)
                        .build();

        marktConditionsService.addMarketCondition(marketConditions);

        Configuration configuration = confService.findByCode(LeverageCodes.Lev_20_15Per.getValue());


        InstrumentMarketStructureConf mainMarket = //
                instMarktStrConfService.findByStructureName(MarketTypes.GOLD_30_MIN.getValue())
                        .stream()
                        .findFirst().get();

        InstrumentMarketStructureConf trendMarket = //
                instMarktStrConfService.findByStructureName(mainMarket.getTrendStructureName())
                        .stream()
                        .findFirst().get();

        MarketStructure structure15Mins = getStructureByTimeFrame(gold4HourStr, TimeFramesUnit.MINUTE.getUnit(), 30);

        CandleBuilder trendIngMarketIndicators = signalBuilder.getCandleBuilder()
                .stream()
                .filter(candleBuilder -> candleBuilder.getTimeFrame().getUnit().equals(trendMarket.getTimeFrameUnit())
                        && candleBuilder.getInterval().intValue() == trendMarket.getTimeFrame())
                .findFirst().get();

        Map<String, IndicatorProcessor> indicatorProcessors = trendIngMarketIndicators.getIndicatorProcessors();


        if (liveInstrumentRate.getAsk() != null)
        {
            IndicatorProcessor EMA_9_5_MINUTE = indicatorProcessors.get("EMA_9_5_MINUTE");

            IndicatorProcessor EMA_20_5_MINUTE = indicatorProcessors.get("EMA_20_5_MINUTE");

            IndicatorProcessor EMA_50_5_MINUTE = indicatorProcessors.get("EMA_50_5_MINUTE");

            IndicatorProcessor EMA_100_5_MINUTE = indicatorProcessors.get("EMA_100_5_MINUTE");

            IndicatorProcessor SMA_9_5_MINUTE = indicatorProcessors.get("SMA_9_5_MINUTE");

            IndicatorProcessor SMA_20_5_MINUTE = indicatorProcessors.get("SMA_20_5_MINUTE");

            IndicatorProcessor SMA_50_5_MINUTE = indicatorProcessors.get("SMA_50_5_MINUTE");

            IndicatorProcessor SMA_100_5_MINUTE = indicatorProcessors.get("SMA_100_5_MINUTE");

            EMA ema = new EMA(EMA_9_5_MINUTE.getValue(), EMA_20_5_MINUTE.getValue(),
                    EMA_50_5_MINUTE.getValue(), EMA_100_5_MINUTE.getValue());

            SMA sma = new SMA(SMA_9_5_MINUTE.getValue(),
                    SMA_20_5_MINUTE.getValue(), SMA_50_5_MINUTE.getValue(), SMA_100_5_MINUTE.getValue());

            MarketPriceBand str = structure15Mins.getUpperBands().stream().findFirst().get();

            Region rr1 = new Region(str.getLowerBound(), str.getUpperBound(), str.getMarketVisitCount());
            Region rr2 = new Region(str.getLowerBound(), str.getUpperBound(), str.getMarketVisitCount());
            Region rr3 = new Region(str.getLowerBound(), str.getUpperBound(), str.getMarketVisitCount());

            Resistance resistance = new Resistance(rr1, rr2, rr3);

            Region sr1 = new Region(str.getLowerBound(), str.getUpperBound(), str.getMarketVisitCount());

            Region sr2 = new Region(str.getLowerBound(), str.getUpperBound(), str.getMarketVisitCount());

            Region sr3 = new Region(str.getLowerBound(), str.getUpperBound(), str.getMarketVisitCount());

            Support support = new Support(sr1, sr2, sr3);

            candleBuilderGold.getMarketStructureManagerCache().getTrendTopic() //
                    .updateTrend(new TrendIndicatorSignal(TimeFramesUnit.MINUTE.getUnit(),
                    30, 0d, 0d, ema, sma, support, resistance));

            MarketPriceBand oneBand = getStructureByTimeFrame(gold4HourStr, trendMarket.getTimeFrameUnit(),
                    trendMarket.getTimeFrame())
                    .getLowerBands().stream().findFirst().get();

            double oneMinBandWidth = Math.abs(oneBand.getUpperBound() - oneBand.getLowerBound());
            double fifteenMinsBandWidth = Math.abs(str.getUpperBound() - str.getLowerBound());

            SRMatrix srMatrix = SRMatrix.builder().creationDate(LocalDateTime.now())
                    .active(true)
                    .l_s_tolerance(str.getLowerBound() - oneMinBandWidth)
                    .support(str.getLowerBound())
                    .r_s_tolerance(str.getLowerBound() + oneMinBandWidth)
                    .l_r_tolerance(str.getUpperBound() - oneMinBandWidth)
                    .resistance(str.getLowerBound())
                    .r_r_tolerance(str.getUpperBound() + oneMinBandWidth)
                    .timeFrameUnit(str.getTimeFrameUnit().getUnit())
                    .timeFrame(str.getTimeFrame())
                    .instrument(inst)
                    .takeProfit(str.getUpperBound() + fifteenMinsBandWidth)
                    .stopLoss(str.getLowerBound() - fifteenMinsBandWidth)
                    .build();
            sRMatrixService.addSRMatrix(srMatrix);
            Position position = Position.builder()
                    .priority(0)
                    .positionType(PositionType.BUY.getValue())
                    .active(true)
                    .currentPositionEquity(50d)
                    .executionCount(1)
                    .percentCapitalDeployed(1.5d)
                    .leverage(configuration.getLev())
                    .configuration(configuration)
                    .strategy(strategy)
                    .takeProfit(srMatrix.getTakeProfit())
                    .stopLoss(srMatrix.getStopLoss())
                    .instrument(inst)
                    .capitalRemainingFirePower(0d)
                    .tradingSessions(null)
                    .portfolioValue(0d)
                    .allowedFirePower(0d)
                    .remainingFirepower(0d)
                    .marketConditions(marketConditions)
                    .srMatrix(srMatrix)
                    .strategy(strategy)
                    .build();
            positionService.add(position);
            strategy.getPositionPnLList().add(position);
            strategyService.updateStrategy(strategy);

            DynamicProcessorUtil orderProcessor = //
                    new DynamicProcessorUtil(orderManagerService, positionService);

            strategy.getPositionPnLList().forEach(el ->
            {
                orderProcessor.process(liveInstrumentRate, el, el.getInstrument());
            });
        }

    }

    private MarketStructure getStructureByTimeFrame(MarketStructure structure, String timeFrame, Integer interval)
    {
        if (structure != null && structure.getTimeFrameUnit().getUnit().equals(timeFrame) //
                && structure.getTimeFrame().intValue() == interval)
        {
            return structure;
        }
        return getStructureByTimeFrame(structure.getChildMarketStructure(), timeFrame, interval);
    }
}


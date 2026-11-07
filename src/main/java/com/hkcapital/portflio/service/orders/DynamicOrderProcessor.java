package com.hkcapital.portflio.service.orders;

import com.hkcapital.portflio.broker.etoro.config.TradingConfiguration;
import com.hkcapital.portflio.market.indicators.CandleBuilder;
import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.market.structure.MarketPriceBand;
import com.hkcapital.portflio.market.structure.MarketStructure;
import com.hkcapital.portflio.market.structure.PriceRange;
import com.hkcapital.portflio.model.*;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portflio.service.candle.etoro.impl.SignalBuilder;
import com.hkcapital.portflio.service.configuration.ConfigurationService;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.marketconditions.MarketConditionsService;
import com.hkcapital.portflio.service.positions.PositionService;
import com.hkcapital.portflio.service.srmatrix.SRMatrixService;
import com.hkcapital.portflio.service.strategy.StrategyService;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
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

    private String trend;

    public DynamicOrderProcessor(InstrumentService instrumentService, ConfigurationService confService, //
                                 StrategyService strategyService, SRMatrixService sRMatrixService, //
                                 MarketConditionsService marktConditionsService, PositionService positionService)
    {
        this.instrumentService = instrumentService;
        this.confService = confService;
        this.strategyService = strategyService;
        this.sRMatrixService = sRMatrixService;
        this.marktConditionsService = marktConditionsService;
        this.positionService = positionService;
    }

    @Override
    public void process(LiveInstrumentRate liveInstrumentRate, SignalBuilder signalBuilder)
    {

        if (!TradingConfiguration.ACTIVATE_AUTOMATIC_TRADING)
        {
            log.info("Skip dynamic orders, activate auto trading.");
            return;
        }

        if (InstrumentRateValidator.isInValidInstrumentRate(liveInstrumentRate))
        {
            return;
        }


        Strategy strategy = strategyService.findDynamicStrategies(true).stream().findFirst().get();

        if(!strategy.getActive())
        {
            log.info("Skip dynamic orders, no active strategy found.");
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
                getStructureByTimeFrame(candleBuilderGold.getMarketStructureManagerCache().getStructures().get("gold_4_hour"),
                        "HOUR", 4);


        Instrument inst = gold4HourStr.getInstrument();

        if (SlippageValidator.isInValidSlippage(liveInstrumentRate, inst))
        {
            return;
        }

        PriceRange range = gold4HourStr.getPriceRange();

        MarketConditions marketConditions =
                MarketConditions.builder()
                        .instrument(inst)
                        .dayLow(range.getLow())
                        .dayHigh(range.getHigh())
                        .percentMove(0d)
                        .build();

        marktConditionsService.addMarketCondition(marketConditions);

        Configuration configuration = confService.findByCode("Lev_20_15Per");

        MarketStructure  structure15Mins = getStructureByTimeFrame(gold4HourStr, "MINUTE", 15);

        MarketPriceBand str = structure15Mins.getUpperBands().stream().findFirst().get();

        SRMatrix srMatrix = SRMatrix.builder().creationDate(LocalDateTime.now())
                .active(true)
                .l_s_tolerance(str.getLowerBound()-2)
                .support(str.getLowerBound())
                .r_s_tolerance(str.getLowerBound()+2)
                .l_r_tolerance(str.getUpperBound()-2)
                .resistance(str.getLowerBound())
                .r_r_tolerance(str.getUpperBound()+2)
                .timeFrameUnit(str.getTimeFrameUnit().getUnit())
                .timeFrame(str.getTimeFrame())
                .instrument(inst)
                .takeProfit(str.getUpperBound()+8)
                .stopLoss(str.getLowerBound()-8)
                .build();

        sRMatrixService.addSRMatrix(srMatrix);

        Position position =  Position.builder()
                .priority(0)
                .positionType("BUY")
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
    }

    private MarketStructure getStructureByTimeFrame(MarketStructure structure, String timeFrame, Integer interval)
    {
        if (structure != null && structure.getTimeFrameUnit().getUnit().equals(timeFrame) && structure.getTimeFrame().intValue() == interval)
        {
            return structure;
        }
        return getStructureByTimeFrame(structure.getChildMarketStructure(), timeFrame, interval);
    }
}


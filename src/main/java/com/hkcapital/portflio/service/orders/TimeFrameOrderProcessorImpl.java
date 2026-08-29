package com.hkcapital.portflio.service.orders;

import com.hkcapital.portflio.broker.etoro.config.TradingConfiguration;
import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.market.structure.MarketStructureCache;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.Position;
import com.hkcapital.portflio.model.SRMatrix;
import com.hkcapital.portflio.model.Strategy;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portflio.service.candle.etoro.impl.SignalBuilder;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.positions.PositionService;
import com.hkcapital.portflio.service.strategy.StrategyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class TimeFrameOrderProcessorImpl implements TimeFrameOrderProcessor
{
    private final Logger logger = LoggerFactory.getLogger(TimeFrameOrderProcessorImpl.class);
    private final InstrumentService instrumentService;
    private final StrategyService strategyService;
    private final PositionService positionService;
    private final OrderManagerService orderManagerService;
    private final MarketStructureCache marketStructureCache;

    public TimeFrameOrderProcessorImpl(InstrumentService instrumentService,
                                       StrategyService strategyService,
                                       PositionService positionService,
                                       MarketStructureCache marketStructureCache,
                                       OrderManagerService orderManagerService)
    {
        this.instrumentService = instrumentService;
        this.strategyService = strategyService;
        this.positionService = positionService;
        this.marketStructureCache = marketStructureCache;
        this.orderManagerService = orderManagerService;
    }

    @Override
    public void process(LiveInstrumentRate instrumentRate, SignalBuilder signalBuilder)
    {
        List<Instrument> instrumentList =
                instrumentService.findAll()
                        .stream()
                        .filter(Instrument::getActive).collect(Collectors.toList());

        if (instrumentRate != null && instrumentRate.getAsk() != null && instrumentRate.getBid() != null)
        {
            for (Instrument instrument : instrumentList)
            {
                if (instrument.getEtoroInstrumentId().intValue() == instrumentRate.getInstrumentId().intValue())
                {
                    if (TradingConfiguration.ACTIVATE_AUTOMATIC_TRADING)
                    {
                        logger.info("Sending Automatic trade to etoro!");
                        Double ask = instrumentRate.getAsk();
                        Double bid = instrumentRate.getBid();
                        Double slippage = ask - bid;
                        logger.info("Instrument price received bid = [{}] , ask = [{}] slippage = [{}] , maxSlippage = [{}] sending order for execution", bid, ask, slippage, instrument.getMaxSlippage());
                        logger.info("No of candles generated {} ",
                                signalBuilder.getCandleBuilder1Min().candles().size());
                        List<Strategy> strategies = //
                                strategyService.findAll()//
                                        .stream()//
                                        .filter(Strategy::getActive)//
                                        .toList();
                        logger.info("no of strategies found {} ", strategies.size());
                        for (Strategy strategy : strategies)
                        {
                            final List<Position> positions = positionService.findByStrategyId(strategy.getId());
                            logger.info("no of positions found {} ", positions.size());
                            for (Position position : positions)
                            {
                                if (position.getActive())
                                {
                                    final SRMatrix srMatrix = position.getSrMatrix();
                                    final Instrument inst = position.getInstrument();
                                    final Integer srTimeFrame = srMatrix.getTimeFrame();
                                    final String srTimeFrameUnit = srMatrix.getTimeFrameUnit();
                                    logger.info("processing positions for S/R timeframe = {} with unit = {}", srTimeFrameUnit, srTimeFrame);
                                    if (srTimeFrameUnit.equals(TimeFramesUnit.HOUR.getUnit()) && srTimeFrame == 4)
                                    {
                                        logger.info("Processing 4 hour timeframe");
                                        TimeFrameOrderProcessor timeFrameOrderProcessor =
                                                new FourHoursTimeFrameOrderProcessorImpl(inst, position,
                                                        orderManagerService,
                                                        positionService,
                                                        marketStructureCache);
                                        timeFrameOrderProcessor.process(instrumentRate, signalBuilder);
                                    }
                                    if (srTimeFrameUnit.equals(TimeFramesUnit.MINUTE.getUnit()) && srTimeFrame == 15)
                                    {
                                        logger.info("Processing 15 minutes timeframe");
                                        TimeFrameOrderProcessor timeFrameOrderProcessor =
                                                new FifteenMinuteTimeFrameOrderProcessorImpl(inst, position,
                                                        orderManagerService,
                                                        positionService,
                                                        marketStructureCache);
                                        timeFrameOrderProcessor.process(instrumentRate, signalBuilder);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

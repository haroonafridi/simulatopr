package com.hkcapital.portflio.service.orders;

import com.hkcapital.portflio.broker.etoro.config.TradingConfiguration;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.Strategy;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portflio.service.candle.etoro.impl.SignalBuilder;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.positions.PositionService;
import com.hkcapital.portflio.service.strategy.StrategyService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

import static com.hkcapital.portflio.service.orders.InstrumentRateValidator.isInValidInstrumentRate;

@Slf4j
public class TimeFrameOrderProcessorImpl implements TimeFrameOrderProcessor
{
    private final InstrumentService instrumentService;
    private final StrategyService strategyService;
    private final PositionService positionService;
    private final OrderManagerService orderManagerService;

    public TimeFrameOrderProcessorImpl(InstrumentService instrumentService,
                                       StrategyService strategyService,
                                       PositionService positionService,
                                       OrderManagerService orderManagerService)
    {
        this.instrumentService = instrumentService;
        this.strategyService = strategyService;
        this.positionService = positionService;
        this.orderManagerService = orderManagerService;
    }

    @Override
    public void process(LiveInstrumentRate instrumentRate, SignalBuilder signalBuilder)
    {

        if (!TradingConfiguration.ACTIVATE_AUTOMATIC_TRADING)
        {
            log.info("Automatic trading is not yet active");
            return;
        }

        if (isInValidInstrumentRate(instrumentRate))
        {
            return;
        }

        final List<Instrument> instrumentList =
                instrumentService.findAll()
                        .stream()
                        .filter(Instrument::getActive)
                        .collect(Collectors.toList());

        for (Instrument instrument : instrumentList)
        {
            if (instrument.getEtoroInstrumentId().intValue() //
                    == instrumentRate.getInstrumentId().intValue())
            {
                final List<Strategy> strategies = //
                        strategyService.findAll()//
                                .stream()//
                                .filter(Strategy::getActive)//
                                .toList();

                log.info("no of strategies found {} ", strategies.size());
                for (final Strategy strategy : strategies)
                {
                    positionService
                            .findValidTradPosition(strategy.getId(), true, 0)
                            .stream()
                            .findFirst().ifPresent(position ->
                                    new TimeFrameOrderFactory(instrumentService, positionService, orderManagerService)
                                            .createTimeFrameOrderProcessor(position)
                                            .process(instrumentRate, signalBuilder)
                            );
                }
            }
        }
    }
}

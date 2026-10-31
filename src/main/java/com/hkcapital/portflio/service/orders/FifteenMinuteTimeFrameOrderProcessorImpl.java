package com.hkcapital.portflio.service.orders;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.Position;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portflio.service.candle.etoro.impl.SignalBuilder;
import com.hkcapital.portflio.service.positions.PositionService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FifteenMinuteTimeFrameOrderProcessorImpl implements OrderProcessor
{
    private final Instrument instrument;
    private final Position position;
    private final OrderManagerService orderManagerService;

    private final PositionService positionService;

    public FifteenMinuteTimeFrameOrderProcessorImpl(Instrument instrument,
                                                    Position position,
                                                    OrderManagerService orderManagerService,
                                                    PositionService positionService)
    {
        this.instrument = instrument;
        this.position = position;
        this.orderManagerService = orderManagerService;
        this.positionService = positionService;
    }

    @Override
    public void process(LiveInstrumentRate liveInstrumentRate, SignalBuilder signalBuilder)
    {
        new FifteenMinutesTimeFrameOrderProcessorUtil(orderManagerService, positionService) //
                .process(liveInstrumentRate, position, instrument);
    }

}

package com.hkcapital.portflio.service.orders;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.Position;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portflio.service.candle.etoro.impl.SignalBuilder;
import com.hkcapital.portflio.service.positions.PositionService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FourHoursTimeFrameOrderProcessorImpl implements TimeFrameOrderProcessor
{
    private final Instrument instrument;
    private final Position position;
    private final OrderManagerService orderManagerService;
    private final PositionService positionService;

    public FourHoursTimeFrameOrderProcessorImpl(final Instrument instrument, Position position,
                                                final OrderManagerService orderManagerService,
                                                final PositionService positionService)
    {
        this.instrument = instrument;
        this.position = position;
        this.orderManagerService = orderManagerService;
        this.positionService = positionService;
    }

    @Override
    public void process(final LiveInstrumentRate liveInstrumentRate, final SignalBuilder signalBuilder)
    {
        process(liveInstrumentRate, position, instrument);
    }

    private void process(final LiveInstrumentRate instrumentRate,
                         final Position position,
                         final Instrument inst)
    {
        new FourHoursTimeFrameOrderProcessorUtil(orderManagerService, positionService)
                .process(instrumentRate, position, inst);
    }
}

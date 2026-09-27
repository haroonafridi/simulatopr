package com.hkcapital.portflio.service.orders;

import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.model.Position;
import com.hkcapital.portflio.model.SRMatrix;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.positions.PositionService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeFrameOrderFactory
{

    private final InstrumentService instrumentService;

    private final PositionService positionService;
    private final OrderManagerService orderManagerService;

    public TimeFrameOrderFactory(InstrumentService instrumentService,
                                 PositionService positionService,
                                 OrderManagerService orderManagerService)
    {
        this.instrumentService = instrumentService;
        this.positionService = positionService;
        this.orderManagerService = orderManagerService;
    }

    public TimeFrameOrderProcessor createTimeFrameOrderProcessor(final Position position)
    {
        final SRMatrix srMatrix = position.getSrMatrix();
        final Integer srTimeFrame = srMatrix.getTimeFrame();
        final String srTimeFrameUnit = srMatrix.getTimeFrameUnit();

        log.info("processing positions for S/R timeframe = {} with unit = {}", srTimeFrameUnit, srTimeFrame);

        if (srTimeFrameUnit.equals(TimeFramesUnit.MINUTE.getUnit()) && srTimeFrame == 15)
        {
            return new FifteenMinuteTimeFrameOrderProcessorImpl(position.getInstrument(), position,
                    orderManagerService,
                    positionService);
        }

        if (srTimeFrameUnit.equals(TimeFramesUnit.HOUR.getUnit()) && srTimeFrame == 4)
        {
            return new FourHoursTimeFrameOrderProcessorImpl(position.getInstrument(), position,
                    orderManagerService,
                    positionService);
        }

        return null;
    }
}

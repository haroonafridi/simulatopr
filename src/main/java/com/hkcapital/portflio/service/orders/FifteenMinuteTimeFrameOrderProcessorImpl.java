package com.hkcapital.portflio.service.orders;

import com.hkcapital.portflio.broker.etoro.dto.order.EtoroMarketOrderDto;
import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.Position;
import com.hkcapital.portflio.model.SRMatrix;
import com.hkcapital.portflio.model.etoro.EtoroOrder;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portflio.service.candle.etoro.impl.SignalBuilder;
import com.hkcapital.portflio.service.orders.impl.etoro.EtoroOrderUtil;
import com.hkcapital.portflio.service.positions.PositionService;
import com.hkcapital.portflio.service.positions.PositionType;
import com.hkcapital.portflio.values.order.OrderStatus;
import com.hkcapital.portflio.values.order.OrderTypes;
import com.hkcapital.portflio.values.timeframe.TimeFrame;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Slf4j
public class FifteenMinuteTimeFrameOrderProcessorImpl implements TimeFrameOrderProcessor
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

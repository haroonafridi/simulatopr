package com.hkcapital.portflio.service.orders;

import com.hkcapital.portflio.broker.etoro.dto.order.EtoroMarketOrderDto;
import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.market.structure.MarketStructureCache;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.Position;
import com.hkcapital.portflio.model.etoro.EtoroOrder;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portflio.service.candle.etoro.impl.SignalBuilder;
import com.hkcapital.portflio.service.orders.impl.etoro.EtoroOrderUtil;
import com.hkcapital.portflio.service.positions.PositionService;
import com.hkcapital.portflio.values.order.OrderStatus;
import com.hkcapital.portflio.values.order.OrderTypes;
import com.hkcapital.portflio.values.timeframe.TimeFrame;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Slf4j
public class FourHoursTimeFrameOrderProcessorImpl implements TimeFrameOrderProcessor
{
    private final Logger logger = LoggerFactory.getLogger(FourHoursTimeFrameOrderProcessorImpl.class);
    private final Instrument instrument;
    private final Position position;
    private final OrderManagerService orderManagerService;
    private final PositionService positionService;
    private final MarketStructureCache marketStructureCache;

    public FourHoursTimeFrameOrderProcessorImpl(Instrument instrument, Position position,
                                                OrderManagerService orderManagerService,
                                                PositionService positionService,
                                                MarketStructureCache marketStructureCache)
    {
        this.instrument = instrument;
        this.position = position;
        this.orderManagerService = orderManagerService;
        this.positionService = positionService;
        this.marketStructureCache = marketStructureCache;
    }

    @Override
    public void process(LiveInstrumentRate liveInstrumentRate, SignalBuilder signalBuilder)
    {
        process(liveInstrumentRate, position, instrument);
    }

    private void process(LiveInstrumentRate instrumentRate, Position position, Instrument inst)
    {
        final Double ask = instrumentRate.getAsk();
        final Double bid = instrumentRate.getBid();
        final Double maxSlippage = inst.getMaxSlippage();
        final Integer leverage = position.getConfiguration().getLev();
        final Double lSupportTol = Math.abs(position.getSrMatrix().getLeftSupportTolerance());
        final Double rSupportTol = Math.abs(position.getSrMatrix().getRightSupportTolerance());
        final Double support = position.getSrMatrix().getSupport();
        final Double lResistanceTol = Math.abs(position.getSrMatrix().getLeftResistanceTolerance());
        final Double rResistanceTol = Math.abs(position.getSrMatrix().getRightResistanceTolerance());
        final Double resistance = position.getSrMatrix().getResistance();
        final TimeFrame timeFrame = new TimeFrame(4, TimeFramesUnit.HOUR.getUnit());
        final Double slippage = Math.abs(ask - bid);
        if (slippage > inst.getMaxSlippage())
        {
            logger.info("Unusual price detected cannot process order slippage = {} , max allowed slippage = {} ", slippage, inst.getMaxSlippage());
            return;
        }
        if ((instrumentRate.getAsk() >= lSupportTol
                && instrumentRate.getAsk() <= rSupportTol)
                && position.getIsLong() &&
                position.getExecutionCount() != null &&
                position.getExecutionCount() > 0)
        {
            logger.info("Placing Buy order for timeframe 4 hour");
            logger.info("Support and price 4 hour timeframe low support = [{}] , high support = [{}]  instrument price = [{}] ", support - 10, support + 10, instrumentRate.getAsk());

            Double tp = position.getSrMatrix().getTakeProfit();
            Double sl = position.getSrMatrix().getStopLoss();

            EtoroMarketOrderDto buyOrder = (EtoroOrderUtil.buildBuyOrder(instrumentRate, maxSlippage,
                    tp, sl, //
                    position, inst, leverage, "Timeframe = 4 HOURS , support = " + support + " Resistance = " + resistance + " " +
                            "bid = " + bid + "ask = " + ask + " SL = " + sl + " TP = " + tp, timeFrame));

            List<EtoroOrder> orders =
                    orderManagerService.findByInstrumentIDAndOderTypeAndStatusAndTimeFrameAndTimeFrameUnitAndIsBuy(buyOrder.getInstrumentId(),
                            OrderTypes.AUTO.getOrderType(), OrderStatus.SENT.getOrderStatus(),
                            buyOrder.getTimeFrame().timeFrame(),
                            buyOrder.getTimeFrame().timeFrameUnit(),
                            buyOrder.getIsBuy());

            if (orders.size() == 0)
            {
                orderManagerService.createAndSaveMarketOrder(buyOrder);
                int executionCount = position.getExecutionCount();
                executionCount = executionCount - 1;
                position.setExecutionCount(executionCount);
                positionService.updatePosition(position);
            }
            return;
        }


        if ((instrumentRate.getBid() >= lResistanceTol && instrumentRate.getBid() <= rResistanceTol)
                && position.getIsShort() &&
                position.getExecutionCount() != null &&
                position.getExecutionCount() > 0) //
        {
            Double sl = position.getSrMatrix().getStopLoss();
            Double tp = position.getSrMatrix().getTakeProfit();
            logger.info("Placing Buy order for timeframe 4 hour");
            EtoroMarketOrderDto saleOrder = EtoroOrderUtil.buildSellOrder(instrumentRate, sl,
                    tp, position, inst, "Timeframe = 15 minute , support = " + support + " Resistance = " + resistance + " " +
                            "bid = " + bid + "ask = " + ask + " SL = " + sl + " TP = " + tp, timeFrame);

            List<EtoroOrder> orders =
                    orderManagerService.findByInstrumentIDAndOderTypeAndStatusAndTimeFrameAndTimeFrameUnitAndIsBuy(saleOrder.getInstrumentId(),
                            OrderTypes.AUTO.getOrderType(), OrderStatus.SENT.getOrderStatus(),
                            saleOrder.getTimeFrame().timeFrame(),
                            saleOrder.getTimeFrame().timeFrameUnit(),
                            saleOrder.getIsBuy());
            if (orders.size() == 0)
            {
                orderManagerService.createAndSaveMarketOrder(saleOrder);
                int executionCount = position.getExecutionCount();
                executionCount = executionCount - 1;
                position.setExecutionCount(executionCount);
                positionService.updatePosition(position);
            }
        }
    }
}

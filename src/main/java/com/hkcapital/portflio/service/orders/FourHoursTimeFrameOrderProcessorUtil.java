package com.hkcapital.portflio.service.orders;

import com.hkcapital.portflio.broker.etoro.dto.order.EtoroMarketOrderDto;
import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.Position;
import com.hkcapital.portflio.model.SRMatrix;
import com.hkcapital.portflio.model.etoro.EtoroOrder;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portflio.service.orders.impl.etoro.EtoroOrderUtil;
import com.hkcapital.portflio.service.positions.PositionService;
import com.hkcapital.portflio.service.positions.PositionType;
import com.hkcapital.portflio.values.order.OrderStatus;
import com.hkcapital.portflio.values.order.OrderTypes;
import com.hkcapital.portflio.values.timeframe.TimeFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.hkcapital.portflio.service.orders.PositionValidator.isInValidPosition;
import static com.hkcapital.portflio.service.orders.SlippageValidator.isInValidSlippage;

@Slf4j
final class FourHoursTimeFrameOrderProcessorUtil
{

    private final OrderManagerService orderManagerService;

    private final PositionService positionService;

    public FourHoursTimeFrameOrderProcessorUtil(final OrderManagerService orderService, //
                                                final PositionService positionService)
    {

        this.orderManagerService = orderService;
        this.positionService = positionService;
    }

    void process(final LiveInstrumentRate instrumentRate, final Position position, final Instrument inst)
    {
        final SRMatrix srMatrix = position.getSrMatrix();
        final Double lSupportTol = Math.abs(srMatrix.getLeftSupportTolerance());
        final Double rSupportTol = Math.abs(srMatrix.getRightSupportTolerance());
        final Double support = srMatrix.getSupport();
        final Double lResistanceTol = Math.abs(srMatrix.getLeftResistanceTolerance());
        final Double rResistanceTol = Math.abs(srMatrix.getRightResistanceTolerance());
        final Double resistance = srMatrix.getResistance();
        final TimeFrame timeFrame = new TimeFrame(4, TimeFramesUnit.HOUR.getUnit());

        if (isInValidSlippage(instrumentRate, inst))
        {
            return;
        }

        if (isInValidPosition(position))
        {
            return;
        }

        log.info("Sending Automatic trade to etoro Timeframe = {} , Timeframe unit = {}", //
                timeFrame.timeFrame(), timeFrame.timeFrameUnit());

        if ((instrumentRate.getAsk() >= lSupportTol
                && instrumentRate.getAsk() <= rSupportTol)
                && position.getPositionType().equals(PositionType.BUY.getValue()))
        {
            log.info("Placing Buy order for timeframe 4 hour");

            final Double tp = position.getSrMatrix().getTakeProfit();
            final Double sl = position.getSrMatrix().getStopLoss();
            final EtoroMarketOrderDto buyOrder = (EtoroOrderUtil.buildBuyOrder(instrumentRate, inst.getMaxSlippage(), tp, sl, //
                    position, inst, position.getConfiguration().getLev(), "Timeframe = 4 HOURS , support = " + support + " Resistance = " + resistance + " " + "bid = " + instrumentRate.getBid() + "ask = " + instrumentRate.getAsk() + " SL = " + sl + " TP = " + tp, timeFrame));

            final List<EtoroOrder> orders = orderManagerService
                    .findEtoroOpenOrders(buyOrder.getInstrumentId(), //
                            OrderTypes.AUTO.getOrderType(), //
                            OrderStatus.SENT.getOrderStatus(), //
                            buyOrder.getTimeFrame().timeFrame(), //
                            buyOrder.getTimeFrame().timeFrameUnit(), //
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


        if ((instrumentRate.getBid() >= lResistanceTol
                && instrumentRate.getBid() <= rResistanceTol)
                && position.getPositionType().equals(PositionType.SELL.getValue())
        ) //
        {
            log.info("Placing Buy order for timeframe 4 hour");
            final Double sl = position.getSrMatrix().getStopLoss();
            final Double tp = position.getSrMatrix().getTakeProfit();
            final EtoroMarketOrderDto saleOrder = EtoroOrderUtil.buildSellOrder(instrumentRate, sl, tp, position, inst, //
                    "Timeframe = 4 hours , support = " + support + " Resistance = " + resistance + " " + "bid = " + instrumentRate.getBid() + "ask = " + instrumentRate.getAsk() + " SL = " + sl + " TP = " + tp, timeFrame);

            final List<EtoroOrder> orders = orderManagerService
                    .findEtoroOpenOrders(saleOrder.getInstrumentId(), //
                            OrderTypes.AUTO.getOrderType(), //
                            OrderStatus.SENT.getOrderStatus(), //
                            saleOrder.getTimeFrame().timeFrame(), //
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

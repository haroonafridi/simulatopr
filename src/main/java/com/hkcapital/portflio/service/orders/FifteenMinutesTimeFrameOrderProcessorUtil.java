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

@Slf4j
final class FifteenMinutesTimeFrameOrderProcessorUtil
{

    private final OrderManagerService orderManagerService;

    private final PositionService positionService;

    public FifteenMinutesTimeFrameOrderProcessorUtil(final OrderManagerService orderService, //
                                                     final PositionService positionService)
    {

        this.orderManagerService = orderService;
        this.positionService = positionService;
    }

    void process(final LiveInstrumentRate instrumentRate, final Position position, final Instrument inst)
    {
        final SRMatrix srMatrix = position.getSrMatrix();
        final Double support = srMatrix.getSupport();
        final Double lSupportTol = Math.abs(srMatrix.getLeftSupportTolerance());
        final Double rSupportTol = Math.abs(srMatrix.getRightSupportTolerance());

        final Double resistance = srMatrix.getResistance();
        final Double lResistanceTol = Math.abs(srMatrix.getLeftResistanceTolerance());
        final Double rResistanceTol = Math.abs(srMatrix.getRightResistanceTolerance());

        final TimeFrame timeFrame = new TimeFrame(15, TimeFramesUnit.MINUTE.getUnit());

        if (SlippageValidator.isInValidSlippage(instrumentRate, inst))
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
            log.info("Buy order successfully placed for Timeframe = 15 minute");
            Double tp = position.getSrMatrix().getTakeProfit();
            Double sl = position.getSrMatrix().getStopLoss();

            EtoroMarketOrderDto buyOrder = (EtoroOrderUtil.buildBuyOrder(instrumentRate, inst.getMaxSlippage(),
                    tp, sl, //
                    position, inst, position.getConfiguration().getLev(), "Timeframe = 15 minute , support = " + support + " Resistance = " + resistance + " " +
                            "bid = " + instrumentRate.getBid() + "ask = " + instrumentRate.getAsk() + " SL = " + sl + " TP = " + tp, timeFrame));

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

        if ((instrumentRate.getBid() >= lResistanceTol
                && instrumentRate.getBid() <= rResistanceTol)
                && position.getPositionType().equals(PositionType.SELL.getValue())) //
        {
            Double sl = position.getSrMatrix().getStopLoss();
            Double tp = position.getSrMatrix().getTakeProfit();

            log.info("Sell order successfully placed for timeframe 4 hour");

            EtoroMarketOrderDto saleOrder = EtoroOrderUtil.buildSellOrder(instrumentRate, sl,
                    tp, position, inst, "Timeframe = 15 minute , support = " + support + " Resistance = " + resistance + " " +
                            "bid = " + instrumentRate.getBid() + "ask = " + instrumentRate.getAsk() + " SL = " + sl + " TP = " + tp, timeFrame);

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

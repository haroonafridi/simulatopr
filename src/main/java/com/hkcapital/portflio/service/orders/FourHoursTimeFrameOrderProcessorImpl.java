package com.hkcapital.portflio.service.orders;

import com.hkcapital.portflio.broker.etoro.dto.order.EtoroMarketOrderDto;
import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.market.structure.MarketStructureCache;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.Position;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portflio.service.candle.etoro.impl.SignalBuilder;
import com.hkcapital.portflio.service.orders.impl.etoro.EtoroOrderUtil;
import com.hkcapital.portflio.values.timeframe.TimeFrame;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class FourHoursTimeFrameOrderProcessorImpl implements TimeFrameOrderProcessor
{
    private final Logger logger = LoggerFactory.getLogger(FourHoursTimeFrameOrderProcessorImpl.class);
    private final Instrument instrument;
    private final Position position;
    private final OrderManagerService orderManagerService;
    private final MarketStructureCache marketStructureCache;

    public FourHoursTimeFrameOrderProcessorImpl(Instrument instrument, Position position,
                                                OrderManagerService orderManagerService,
                                                MarketStructureCache marketStructureCache)
    {
        this.instrument = instrument;
        this.position = position;
        this.orderManagerService = orderManagerService;
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
        logger.info("Support and price 4 hour timeframe low support = [{}] , high support = [{}]  instrument price = [{}] ", support - 10, support + 10, instrumentRate.getAsk());
        if ((instrumentRate.getAsk() >= support - lSupportTol
                && instrumentRate.getAsk() <= support + rSupportTol)
                && position.getIsLong())
        {
            logger.info("Buy order successfully placed for timeframe 4 hour");
            Double sl = position.getStopLoss();
            Double tp = position.getTakeProfit();
            orderManagerService.createAndSaveMarketOrder((EtoroOrderUtil.buildBuyOrder(instrumentRate, maxSlippage,
                    sl, tp, //
                    position, inst, leverage, "Timeframe = 4 hour , support = " + support + " Resistance = " + resistance + " " +
                            "bid = " + bid + "ask = " + ask + " SL = " + sl + " TP = " + tp, timeFrame)));
            return;
        }

        if ((instrumentRate.getBid() >= resistance - lResistanceTol
                && instrumentRate.getBid() <= resistance + rResistanceTol)
                && position.getIsShort()
        ) //
        {
            Double sl = position.getStopLoss();
            Double tp = position.getTakeProfit();
            logger.info("Sell order successfully placed for timeframe 4 hour");
            EtoroMarketOrderDto marketOrder = EtoroOrderUtil.buildSellOrder(instrumentRate, sl,
                    tp, position, inst, "Timeframe = 4 hour , support = " + support + " Resistance = " + resistance + " " +
                            "bid = " + bid + "ask = " + ask + " SL = " + sl + " TP = " + tp, timeFrame);
            orderManagerService.createAndSaveMarketOrder(marketOrder);
        }
    }
}
